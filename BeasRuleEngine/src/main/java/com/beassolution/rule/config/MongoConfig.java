package com.beassolution.rule.config;

import com.beassolution.rule.listener.MongoEventListeners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * MongoDB configuration class for the Beas Rule Engine.
 * 
 * <p>This class configures the MongoDB connection and template settings including:
 * <ul>
 *   <li>Custom MongoTemplate configuration</li>
 *   <li>Type mapper settings for document serialization</li>
 *   <li>Event listener registration</li>
 *   <li>Converter configuration</li>
 * </ul>
 * 
 * <p>The configuration ensures proper handling of MongoDB documents and
 * enables lifecycle events for data operations.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class MongoConfig {
    
    /**
     * Creates and configures the MongoTemplate with custom settings.
     * 
     * <p>This bean configures the MongoTemplate with specific converter and
     * type mapper settings optimized for the rule engine application.
     * 
     * <p>Configuration includes:
     * <ul>
     *   <li>Custom MappingMongoConverter with type mapper</li>
     *   <li>Entity lifecycle events enabled</li>
     *   <li>Event listener registration</li>
     * </ul>
     * 
     * @param mongodbDbFactory MongoDB database factory
     * @param mongoMappingContext MongoDB mapping context
     * @param context Spring application context
     * @param mongoEventListeners MongoDB event listeners
     * @return Configured MongoTemplate instance
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongodbDbFactory,
                                       MongoMappingContext mongoMappingContext,
                                       AbstractApplicationContext context,
                                       MongoEventListeners mongoEventListeners
    ) {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongodbDbFactory),
                mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(mongodbDbFactory, converter);

        mongoTemplate.setEntityLifecycleEventsEnabled(true);
        context.addApplicationListener(mongoEventListeners);
        return mongoTemplate;
    }

}
