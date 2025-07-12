package com.beassolution.rule.engine;

import com.beassolution.rule.exception.OperationException;
import com.beassolution.rule.model.RuleHelper;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Component for dynamically loading and instantiating helper classes.
 * 
 * <p>This class is responsible for loading helper classes from JAR files
 * and creating instances of them for use in rule execution. It uses
 * ClassGraph to scan JAR files and dynamically load classes.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Dynamic JAR file loading</li>
 *   <li>Class scanning and instantiation</li>
 *   <li>Package-based class filtering</li>
 *   <li>Error handling for instantiation failures</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InstanceInitiator {
    
    /**
     * Creates instances of helper classes from a JAR file.
     * 
     * <p>This method loads a JAR file from the specified URL and creates
     * instances of all concrete classes in the specified package path.
     * The instances are returned as a map with class names as keys.
     * 
     * @param helper The rule helper configuration containing JAR URL and package path
     * @return Map of class names to instantiated objects
     * @throws OperationException if loading or instantiation fails
     */
    public Map<String, Object> create(RuleHelper helper) {
        try {
            log.info("{} jar file loading...", helper.getPackageUrl());
            URLClassLoader loader = new URLClassLoader(new URL[]{new URL(helper.getPackageUrl())}, getClass().getClassLoader());
            Map<String, Object> objects = initialize(loader, helper.getPackagePath());
            log.info("{} jar file loaded.", helper.getPackageUrl());
            return objects;
        } catch (Exception e) {
            log.error("Helper instance error!", e);
            throw new OperationException(e);
        }
    }

    /**
     * Initializes helper class instances from a class loader.
     * 
     * <p>This method scans the specified package path within the class loader
     * and creates instances of all concrete, non-abstract classes. The classes
     * must have a no-argument constructor for instantiation.
     * 
     * @param classLoader The class loader containing the helper classes
     * @param packagePath The package path to scan for classes
     * @return Map of class names to instantiated objects
     * @throws OperationException if no classes are found or instantiation fails
     */
    private Map<String, Object> initialize(ClassLoader classLoader, String packagePath) {

        try (ScanResult scanResult =
                     new ClassGraph()
                             .enableRemoteJarScanning()
                             .enableAllInfo()
                             .addClassLoader(classLoader)
                             .acceptPackages(packagePath)
                             .scan()) {
            Map<String, Object> vars = new HashMap<>();
            ClassInfoList listOfClasses = scanResult.getAllClasses();
            if (listOfClasses.isEmpty())
                throw new OperationException(packagePath + " there is no any class");
            for (ClassInfo classInfo : listOfClasses) {
                Class<?> clazz = classInfo.loadClass();
                if (!clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) &&
                        Objects.equals(clazz.getPackageName(), packagePath)) {
                    try {
                        log.info("Class initialization... {} / {}", clazz.getPackageName(), clazz.getName());
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        vars.put(StringUtils.uncapitalize(clazz.getSimpleName()), instance);
                        log.info("Class initialized. {} / {}", clazz.getPackageName(), clazz.getName());
                    } catch (Exception e) {
                        log.error("Helper init err!", e);
                        throw new OperationException(clazz.getName() + "->" + e.getMessage() + " initialization error!");
                    }
                }
            }
            return vars;
        }
    }
}
