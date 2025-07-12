package com.beassolution.rule.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;

/**
 * Custom RSQL comparison operators for the Beas Rule Engine.
 * 
 * <p>This class defines custom comparison operators that extend the standard
 * RSQL operators to provide additional querying capabilities for MongoDB
 * operations. These operators are used in RSQL queries for filtering entities.
 * 
 * <p>Supported operators include:
 * <ul>
 *   <li>Equality operators (==, !=)</li>
 *   <li>Comparison operators (&gt;, &gt;=, &lt;, &lt;=)</li>
 *   <li>Collection operators (in, out)</li>
 *   <li>Pattern matching operators (like, notlike)</li>
 *   <li>Null check operators (isEmpty)</li>
 * </ul>
 * 
 * @author Beas Solution Team
 * @version 1.0
 * @since 1.0
 */
public class EntityRSQLOperators {
    
    /**
     * Equality operator for exact matches.
     */
    public static final ComparisonOperator EQUAL = new ComparisonOperator(new String[]{"=="});
    
    /**
     * Inequality operator for non-matches.
     */
    public static final ComparisonOperator NOT_EQUAL = new ComparisonOperator(new String[]{"!="});
    
    /**
     * Greater than operator.
     */
    public static final ComparisonOperator GREATER_THAN = new ComparisonOperator(new String[]{"=gt=", ">"});
    
    /**
     * Greater than or equal operator.
     */
    public static final ComparisonOperator GREATER_THAN_OR_EQUAL = new ComparisonOperator(new String[]{"=ge=", ">="});
    
    /**
     * Less than operator.
     */
    public static final ComparisonOperator LESS_THAN = new ComparisonOperator(new String[]{"=lt=", "<"});
    
    /**
     * Less than or equal operator.
     */
    public static final ComparisonOperator LESS_THAN_OR_EQUAL = new ComparisonOperator(new String[]{"=le=", "<="});
    
    /**
     * In operator for collection membership.
     */
    public static final ComparisonOperator IN = new ComparisonOperator("=in=", false);
    
    /**
     * Not in operator for collection non-membership.
     */
    public static final ComparisonOperator NOT_IN = new ComparisonOperator("=out=", false);
    
    /**
     * Like operator for pattern matching.
     */
    public static final ComparisonOperator LIKE = new ComparisonOperator("=like=", false);
    
    /**
     * Not like operator for pattern non-matching.
     */
    public static final ComparisonOperator NOT_LIKE = new ComparisonOperator("=notlike=", false);

    /**
     * Is empty operator for null/empty checks.
     */
    public static final ComparisonOperator IS_EMPTY = new ComparisonOperator("=isEmpty=", false);
}