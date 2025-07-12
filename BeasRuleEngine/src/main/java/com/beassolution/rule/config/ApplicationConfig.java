package com.beassolution.rule.config;

import com.beassolution.rule.rsql.EntityRSQLOperators;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Main application configuration class for the Beas Rule Engine.
 * 
 * <p>This class provides core configuration beans for the application including:
 * <ul>
 *   <li>RSQL parser configuration for advanced querying</li>
 *   <li>ModelMapper configuration for object mapping</li>
 *   <li>Custom comparison operators for RSQL queries</li>
 * </ul>
 * 
 * <p>The RSQL parser is configured with extended operators to support
 * complex querying capabilities including filtering, sorting, and pagination.
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class ApplicationConfig {
    
    /**
     * Creates and configures the RSQL parser with custom operators.
     * 
     * <p>This bean configures the RSQL parser with extended comparison operators
     * that support advanced querying capabilities. The parser includes both
     * standard RSQL operators and custom operators for specific use cases.
     * 
     * <p>Supported operators include:
     * <ul>
     *   <li>Equality: ==, !=</li>
     *   <li>Comparison: =gt=, =ge=, =lt=, =le=</li>
     *   <li>Collection: =in=, =out=</li>
     *   <li>Pattern matching: =like=, =notlike=</li>
     *   <li>Null checks: =isEmpty=</li>
     * </ul>
     * 
     * @return Configured RSQLParser instance with custom operators
     */
    @Bean
    public RSQLParser rsqlParser() {
        Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
        operators.add(EntityRSQLOperators.IN);
        operators.add(EntityRSQLOperators.NOT_IN);
        operators.add(EntityRSQLOperators.EQUAL);
        operators.add(EntityRSQLOperators.NOT_EQUAL);
        operators.add(EntityRSQLOperators.GREATER_THAN);
        operators.add(EntityRSQLOperators.GREATER_THAN_OR_EQUAL);
        operators.add(EntityRSQLOperators.LESS_THAN);
        operators.add(EntityRSQLOperators.LESS_THAN_OR_EQUAL);
        operators.add(EntityRSQLOperators.LIKE);
        operators.add(EntityRSQLOperators.NOT_LIKE);
        operators.add(EntityRSQLOperators.IS_EMPTY);

        return new RSQLParser(operators);
    }

    /**
     * Creates and configures the ModelMapper for object mapping.
     * 
     * <p>This bean configures the ModelMapper with specific settings for
     * optimal object mapping behavior in the rule engine context.
     * 
     * <p>Configuration includes:
     * <ul>
     *   <li>Skip null values during mapping</li>
     *   <li>Enable deep copying for nested objects</li>
     *   <li>Enable collection merging</li>
     *   <li>Require full type matching for safety</li>
     * </ul>
     * 
     * @return Configured ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setDeepCopyEnabled(true)
                .setCollectionsMergeEnabled(true)
                .setFullTypeMatchingRequired(true);
        return modelMapper;
    }
}
