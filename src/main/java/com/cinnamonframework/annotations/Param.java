/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinnamonframework.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Andres Jaimes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface Param {
    
    /**
     * The name of the request parameter to bind this argument to.
     * @return 
     */
    String name();
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: false.
     * @return 
     */
    boolean defaultBoolean() default false;

    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: 0.0.
     * @return 
     */
    double defaultDouble() default 0.0;

    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: 0.0f.
     * @return 
     */
    float defaultFloat() default 0.0f;
    
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: 0.
     * @return 
     */
    int defaultInt() default 0;
    
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: 0L.
     * @return 
     */
    long defaultLong() default 0L;
    
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: 0.
     * @return 
     */
    short defaultShort() default 0;
    
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: 0.
     * @return 
     */
    byte defaultByte() default 0;
    
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: "" (empty string).
     * @return 
     */
    String defaultString() default "";
    
    
    /**
     * The value to use if the request parameter does not exist or it is empty.
     * Default value: {} (empty array).
     * @return 
     */
    String[] defaultStringArray() default {};
    
    int minInt() default Integer.MIN_VALUE;
    int maxInt() default Integer.MAX_VALUE;
    double minDouble() default Double.MIN_VALUE;
    double maxDouble() default Double.MAX_VALUE;
    short minShort() default Short.MIN_VALUE;
    short maxShort() default Short.MAX_VALUE;
    byte minByte() default Byte.MIN_VALUE;
    byte maxByte() default Byte.MAX_VALUE;
    long minLong() default Long.MIN_VALUE;
    long maxLong() default Long.MAX_VALUE;
    float minFloat() default Float.MIN_VALUE;
    float maxFloat() default Float.MAX_VALUE;
    
    
    /**
     * The minimum length accepted for a string. Default value: 0.
     * @return 
     */
    int minLength() default 0;
    
    
    /**
     * The max length accepted for a string. A value of -1 means no limit.
     * Default value: -1.
     * @return 
     */
    int maxLength() default -1;
    
    
    /**
     * A regular expression for string validation. This validation is skipped
     * if value is empty. Default value: empty.
     * @return 
     */
    String regex() default "";
    String message() default "The value entered is not valid."; 
    
    boolean required() default false;
        
    /*boolean notEmpty() default false;
    boolean notBlank() default false;
    */
    
    //String defaultValue() default ""; // like this ???

    
   // 
   // String  bundle() default ""; a bundle from where we'll get the
    // message pointed  by message or if empty we will display message verbatim

}
