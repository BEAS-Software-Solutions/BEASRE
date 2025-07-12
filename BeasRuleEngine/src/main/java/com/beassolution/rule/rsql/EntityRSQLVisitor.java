package com.beassolution.rule.rsql;

import cz.jirutka.rsql.parser.ast.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * RSQL visitor for converting RSQL queries to MongoDB criteria.
 * 
 * <p>This class implements the RSQL visitor pattern to convert RSQL query
 * nodes into MongoDB criteria objects. It supports all custom operators
 * defined in EntityRSQLOperators and handles complex query structures.
 * 
 * <p>Key features include:
 * <ul>
 *   <li>RSQL to MongoDB criteria conversion</li>
 *   <li>Support for logical operators (AND, OR)</li>
 *   <li>Support for comparison operators</li>
 *   <li>Type casting and value conversion</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class EntityRSQLVisitor implements RSQLVisitor<Query, Query> {

    /**
     * Visits AND nodes and creates MongoDB AND criteria.
     * 
     * @param andNode The AND node to process
     * @param param The query to add criteria to
     * @return The modified query with AND criteria
     */
    @Override
    public Query visit(AndNode andNode, Query param) {
        final Criteria rootCriteria = new Criteria();
        List<Criteria> criteriaList = andNode.getChildren().stream().filter(node -> node instanceof ComparisonNode)
                .map(node -> createCriteria((ComparisonNode) node)).collect(Collectors.toList());

        return param.addCriteria(rootCriteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
    }

    /**
     * Visits OR nodes and creates MongoDB OR criteria.
     * 
     * @param orNode The OR node to process
     * @param param The query to add criteria to
     * @return The modified query with OR criteria
     */
    @Override
    public Query visit(OrNode orNode, Query param) {
        final Criteria rootCriteria = new Criteria();
        List<Criteria> criteriaList = orNode.getChildren().stream().filter(node -> node instanceof ComparisonNode)
                .map(node -> createCriteria((ComparisonNode) node)).collect(Collectors.toList());
        return param.addCriteria(rootCriteria.orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
    }

    /**
     * Visits comparison nodes and creates MongoDB criteria.
     * 
     * @param comparisonNode The comparison node to process
     * @param param The query to add criteria to
     * @return The modified query with comparison criteria
     */
    @Override
    public Query visit(ComparisonNode comparisonNode, Query param) {
        return param.addCriteria(createCriteria(comparisonNode));
    }

    /**
     * Creates MongoDB criteria from a comparison node.
     * 
     * <p>This method converts RSQL comparison operators to MongoDB criteria
     * based on the operator type. It handles all supported operators including
     * equality, comparison, collection, and pattern matching operators.
     * 
     * @param comparisonNode The comparison node to convert
     * @return The MongoDB criteria
     */
    public Criteria createCriteria(ComparisonNode comparisonNode) {
        ComparisonOperator queryOperator = comparisonNode.getOperator();
        Criteria criteria = Criteria.where(comparisonNode.getSelector());
        Object value = null;
        if (comparisonNode.getOperator().isMultiValue())
            value = comparisonNode.getArguments().stream().map(v -> castValue(v)).collect(Collectors.toList());
        else
            value = castValue(comparisonNode.getArguments().get(0));

        if (Objects.equals(queryOperator, EntityRSQLOperators.EQUAL))
            return criteria.is(value);

        if (Objects.equals(queryOperator, EntityRSQLOperators.NOT_EQUAL)) {
            return criteria.ne(value);
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.IN)) {
            if (value instanceof List)
                return criteria.in(((List) value).toArray());
            return criteria.in(value);

        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.NOT_IN)) {
            if (value instanceof List) {
                return criteria.nin(((List) value).toArray());
            }
            return criteria.nin(value);
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.GREATER_THAN)) {
            return criteria.gt(value);
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.GREATER_THAN_OR_EQUAL)) {
            return criteria.gte(value);
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.LESS_THAN)) {
            return criteria.lt(value);
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.LESS_THAN_OR_EQUAL)) {
            return criteria.lte(value);
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.LIKE)) {
            return criteria.regex(Objects.requireNonNull(MongoRegexCreator.INSTANCE.toRegularExpression(String.valueOf(value),
                    MongoRegexCreator.MatchMode.LIKE)));
        }
        if (Objects.equals(queryOperator, EntityRSQLOperators.NOT_LIKE)) {
            return criteria.not().regex(Objects.requireNonNull(MongoRegexCreator.INSTANCE.toRegularExpression(String.valueOf(value),
                    MongoRegexCreator.MatchMode.LIKE)));
        }

        log.info("Criteria operator not found!");
        return criteria;
    }

    /**
     * Casts string values to appropriate types for MongoDB queries.
     * 
     * <p>This method converts string values from RSQL queries to appropriate
     * Java types based on the content. It handles numbers, booleans, and strings.
     * 
     * @param v The string value to cast
     * @return The casted value
     */
    public Object castValue(String v) {
        if (Objects.isNull(v) || Objects.equals(v, "null")) {
            return null;
        }
        v = v.trim();
        if (v.contains("'")) {
            return v.replace("'", "");
        }

        if (NumberUtils.isParsable(v)) {
            if (v.contains(".")) {
                return NumberUtils.createFloat(v);
            }
            Long longValue = Long.parseLong(v);
            if (longValue > Integer.MAX_VALUE) {
                return longValue;
            }
            return NumberUtils.createInteger(v);
        }

        if (Objects.equals(v.toLowerCase(), "true") | Objects.equals(v.toLowerCase(), "false")) {
            return Boolean.parseBoolean(v.toLowerCase());
        }

        return v;
    }
}
