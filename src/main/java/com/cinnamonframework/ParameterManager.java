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
package com.cinnamonframework;

import com.cinnamonframework.annotations.Bind;
import com.cinnamonframework.annotations.Param;
import com.cinnamonframework.annotations.ParamMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles parameters in Cinnamon user's classes, assigning requested method
 * parameters and converting them to the right types.
 * 
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
class ParameterManager {

    private static final String __boolean = "boolean";
    private static final String __byte = "byte";
    private static final String __double = "double";
    private static final String __float = "float";
    private static final String __int = "int";
    private static final String __long = "long";
    private static final String __short = "short";
    private static final String __classBoolean = "java.lang.Boolean";
    private static final String __classByte = "java.lang.Byte";
    private static final String __classDouble = "java.lang.Double";
    private static final String __classFloat = "java.lang.Float";
    private static final String __classInt = "java.lang.Integer";
    private static final String __classLong = "java.lang.Long";
    private static final String __classShort = "java.lang.Short";
    private static final String __classString = "java.lang.String";
    private static final String __classStringArray = "[Ljava.lang.String;";

    
    
    /**
     * Looks for annotations in method parameters and populates them 
     * with values from Http Request object.
     * @param method
     * @param httpServletRequest
     * @return 
     */
    static Object[] getParameters(Controller instance, Method method, HttpServletRequest httpServletRequest, UrlAnalyzer urlAnalyzer)
        throws ParameterException, UnsupportedTypeException, NonMatchingAnnotationsException, UrlNotFoundException {

        int p = 0;
        List<Object> objects = new ArrayList<>();
        Map<String, String> urlParams = getParamMappings(method, httpServletRequest, urlAnalyzer);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class[] parameterTypes = method.getParameterTypes();
        
        for (Annotation[] annotations : parameterAnnotations) {

            Class parameterType = parameterTypes[p];

            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    p++; // increase only when annotation is instance of Param or Bind
                    Param param = (Param) annotation;
                    String s;
                    if (urlParams.get(param.name()) != null) {
                        s = urlParams.get(param.name());
                    }   
                    else {
                        s = httpServletRequest.getParameter(param.name());
                    }
                    
                    switch (parameterType.getName()) {
                        case __classString:
                            objects.add(getString(s, param, instance.messages));
                            break;
                        case __boolean:
                        case __classBoolean:
                            objects.add(getBoolean(s));
                            break;
                        case __int:
                        case __classInt:
                            objects.add(getInteger(s, param, instance.messages));
                            break;
                        case __long:
                        case __classLong:
                            objects.add(getLong(s, param, instance.messages));
                            break;
                        case __double:
                        case __classDouble:
                            objects.add(getDouble(s, param, instance.messages));
                            break;
                        case __classStringArray:
                            objects.add(getStringArray(httpServletRequest, param));
                            break;
                        case __float:
                        case __classFloat:
                            objects.add(getFloat(s, param, instance.messages));
                            break;
                        case __short:
                        case __classShort:
                            objects.add(getShort(s, param, instance.messages));
                            break;
                        case __byte:
                        case __classByte:
                            objects.add(getByte(s, param, instance.messages));
                            break;
                        default:
                            throw new UnsupportedTypeException(
                                "Type \"" + parameterType.getName() + "\" in parameter list is not supported. Valid types include boolean, double, float, int, long, short, String, String[]."); 
                    }
                }
                else if (annotation instanceof Bind) {
                    p++; // increase only when annotation is instance of Param or Bind
                    objects.add(getObject(parameterType, httpServletRequest, instance.messages));
                }
            }
        }
        
        if (parameterTypes.length != p) {
            throw new NonMatchingAnnotationsException(
                "All parameters in method must be annotated with Param.");
        }
        
        return objects.toArray();
    }
    
    
    /**
     * Returns a String value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return a String value.
     */
    private static String getString(String s, Param param, Messages messages) {
        if (s == null) {
            s = param.defaultString();
        }
        else {
            if (s.length() < param.minLength()) {
                messages.addMessage(param.name(), param.message());
            }
            if (param.maxLength() >= 0) {
                if (s.length() > param.maxLength()) {
                    messages.addMessage(param.name(), param.message());
                }
            }
            if (param.regex().length() > 0) {
                if (!Pattern.matches(param.regex(), s)) {
                    messages.addMessage(param.name(), param.message());
                }
            }
        }
        return s;
    }
    
    
    /**
     * Returns a boolean value from the string specified.
     * @param s a string containing the value to convert.
     * @return a boolean value.
     */
    private static boolean getBoolean(String s) {
        return Boolean.parseBoolean(s);
    }
    
    
    /**
     * Returns an byte value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return an byte value.
     */
    private static byte getByte(String s, Param param, Messages messages) {
        byte b;
        try {
            b = Byte.parseByte(s);
            if (!(b >= param.minByte() && b <= param.maxByte())) {
                messages.addMessage(param.name(), param.message());
            }
        } catch (NullPointerException | NumberFormatException e) {
            b = param.defaultByte();
        }
        return b;
    }


    /**
     * Returns a double value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return a double value.
     */
    private static double getDouble(String s, Param param, Messages messages) {
        double d;
        try { 
            d = Double.parseDouble(s);
            if (!(d >= param.minDouble() && d <= param.maxDouble())) {
                messages.addMessage(param.name(), param.message());
            }
        } 
        catch (NullPointerException | NumberFormatException e) {
            d = param.defaultDouble();
        }
        return d;
    }
    
    
    /**
     * Returns a float value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return a float value.
     */
    private static float getFloat(String s, Param param, Messages messages) {
        float f;
        try { 
            f = Float.parseFloat(s); 
            if (!(f >= param.minFloat() && f <= param.maxFloat())) {
                messages.addMessage(param.name(), param.message());
            }
        } 
        catch (NullPointerException | NumberFormatException e) {
            f = param.defaultFloat();
        }
        return f;
    } 
    
    
    /**
     * Returns an int value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return an int value.
     */
    private static int getInteger(String s, Param param, Messages messages) {
        int i;
        try {
            i = Integer.parseInt(s);
            if (!(i >= param.minInt() && i <= param.maxInt())) {
                messages.addMessage(param.name(), param.message());
            }
        } catch (NullPointerException | NumberFormatException e) {
            i = param.defaultInt();
        }
        return i;
    }
    
    
    /**
     * Returns a long value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return a long value.
     */
    private static long getLong(String s, Param param, Messages messages) {
        long l;
        try {
            l = Long.parseLong(s);
            if (!(l >= param.minLong() && l <= param.maxLong())) {
                messages.addMessage(param.name(), param.message());
            }
        } catch (NullPointerException | NumberFormatException e) {
            l = param.defaultLong();
        }
        return l;
    }
    
    
    /**
     * Returns a short value, considering the constraints given by Param.
     * @param s a string containing the value to convert.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @param messages a Messages instance where errors during conversion will
     *        be posted.
     * @return a short value.
     */
    private static short getShort(String s, Param param, Messages messages) {
        short sh;
        try {
            sh = Short.parseShort(s);
            if (!(sh >= param.minShort() && sh <= param.maxShort())) {
                messages.addMessage(param.name(), param.message());
            }
        } catch (NullPointerException | NumberFormatException e) {
            sh = param.defaultShort();
        }
        return sh;
    }
    
    
    /**
     * Returns a String[] value, considering the constraints given by Param.
     * @param httpServletRequest a request where the array of values will
     *        be extracted from.
     * @param param a Param annotation containing a group of default values and
     *        constraints.
     * @return a String[] value.
     */
    private static String[] getStringArray(HttpServletRequest httpServletRequest, Param param) {
        String[] ss = httpServletRequest.getParameterValues(param.name());
        if (ss == null) ss = param.defaultStringArray();
        return ss;
    }
    
    
    /**
     * Creates an instance and populates.
     * This method ignores setters not marked as public or that contain more
     * than one parameter.
     * @param <T>
     * @param type
     * @param httpServletRequest
     * @param messages
     * @return
     * @throws UnsupportedTypeException 
     */
    private static <T> T getObject(Class<T> type, HttpServletRequest httpServletRequest, Messages messages) 
        throws ParameterException, UnsupportedTypeException {
        
        try {
            T instance = (T) type.newInstance();
            Method[] methods = type.getMethods();
            
            for (Method method : methods) {
                String name = method.getName();
                
                if (name.startsWith("set") && name.length() > 3) {
                    
                    List<Object> objects = new ArrayList<>();
                    Annotation[] annotations = method.getAnnotations();
                    Class[] parameterTypes = method.getParameterTypes();

                    if (parameterTypes.length != 1) continue; // ignore setters that do not have just 1 argument

                    Class parameterType = parameterTypes[0];

                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Param) {

                            Param param = (Param) annotation;
                            String s = httpServletRequest.getParameter(param.name());

                            if (s != null) {
                                switch (parameterType.getName()) {
                                    case __classString:
                                        objects.add(getString(s, param, messages));
                                        break;
                                    case __boolean:
                                    case __classBoolean:
                                        objects.add(getBoolean(s));
                                        break;
                                    case __int:
                                    case __classInt:
                                        objects.add(getInteger(s, param, messages));
                                        break;
                                    case __long:
                                    case __classLong:
                                        objects.add(getLong(s, param, messages));
                                        break;
                                    case __double:
                                    case __classDouble:
                                        objects.add(getDouble(s, param, messages));
                                        break;
                                    case __classStringArray:
                                        objects.add(getStringArray(httpServletRequest, param));
                                        break;
                                    case __float:
                                    case __classFloat:
                                        objects.add(getFloat(s, param, messages));
                                        break;
                                    case __short:
                                    case __classShort:
                                        objects.add(getShort(s, param, messages));
                                        break;
                                    case __byte:
                                    case __classByte:
                                        objects.add(getByte(s, param, messages));
                                        break;
                                    default:
                                        throw new UnsupportedTypeException(
                                            "Type \"" + parameterType.getName() + "\" in parameter list is not supported. Valid types include boolean, double, float, int, long, short, String, String[]."); 
                                }

                                try {
                                    method.invoke(instance, objects.toArray());
                                }
                                catch (NullPointerException | SecurityException | InvocationTargetException e) {
                                    // just ignore and move to the next value
                                }
                            }
                            else if (param.required()) {
                                messages.addMessage(param.name(), param.message());
                            }

                        }

                    }

                }
                
            }
            
            return instance;
        }
        catch (IllegalAccessException | InstantiationException e) {
            throw new ParameterException(
                "Cannot instantiate \"" +
                type.getClass().getName() + "\": Class not found or access not allowed. Verify it has the \"public\" modifier.", e);
        }
    }

    
    
    static Map<String, String> getParamMappings(Method method, HttpServletRequest httpServletRequest, UrlAnalyzer urlAnalyzer) 
        throws UrlNotFoundException {
        
        Map<String, String> params = new HashMap<>();
        Annotation[] methodAnnotations = method.getAnnotations();
        
        for (Annotation annotation : methodAnnotations) {
            if (annotation instanceof ParamMapping) {
                ParamMapping paramMapping = (ParamMapping) annotation;
                String mapping = paramMapping.value();
                if (mapping.startsWith("/") && mapping.length() > 1) {
                    mapping = mapping.substring(1);
                }
                String[] parts = mapping.split("/");
                
                if (parts.length != urlAnalyzer.getParameters().length) {
                    throw new UrlNotFoundException("The requested resource was not found.");
                }
                
                for (int i = 0; i < parts.length; i++) {
                    params.put(parts[i], urlAnalyzer.getParameters()[i]);
                }
                    
                break;
            }
        }
        
        return params;
        
    }
   
}
