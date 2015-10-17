/* 
 * Cinnamon Framework
 * Copyright (c) 2014, Andres Jaimes (http://andres.jaimes.net)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of Cinnamon Framework nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.cinnamonframework.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
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
