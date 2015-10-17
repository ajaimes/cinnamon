/* 
 * Cinnamon Framework
 * Copyright (c) 2014, Andres Jaimes Velazquez
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

import com.cinnamonframework.annotations.ParamMapping;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
class ControllerManager {
    
    /**
     * 
     * @param className
     * @return
     * @throws ServerException if className is null or an instantiation or illegal
     *         access exception occur.
     * @throws ClassNotFoundException if class name cannot be found.
     */
    static Controller instantiateController(String className) throws UrlNotFoundException, ServerException {
        
        if (className == null) {
            throw new ServerException(
                "Parameter className cannot be null.");
        }
        
        try {
            return (Controller) Class.forName(className).newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException e) {
            throw new UrlNotFoundException(
                "Class \"" +
                className + "\" not found or access not allowed.", e);
        }
        catch (InstantiationException e) {
            throw new ServerException(
                "Class \""
               + className + "\" cannot be instantiated: "
               + e.getMessage(), e);
        }
        catch (ClassCastException e) {            
            // ClassCastException might be thrown if the requested class exists, but does not extend Controller.
            throw new ServerException(
                "Class \""
               + className + "\" does not extend \"com.cinnamonframework.Controller\".", e);
        }
        
    }
    
    /**
     * Performs the Controller's method invocation.
     * Consider adding the following line if multiple calls will be
     * performed to the reflected method. Since we just do one call, it is more 
     * expensive to enable it. My sample times WITHOUT it are: 7660 ns, 5439 ns, 8240 ns.
     * My sample times WITH it are: 19130 ns, 9400 ns, 14516 ns.
     * As you can see there's a penalty if you call it.
     * 
     * method.setAccessible(true);
     * 
     * To perform the test, first I logged the time before and after invoking
     * the method. Then for a second test, log before and after, but include the
     * setAccessible call in-between.
     * 
     * @param instance
     * @param urlAnalyzer
     * @return
     * @throws UrlNotFoundException
     * @throws ServerException 
     */
    static Result invoke(Controller instance, UrlAnalyzer urlAnalyzer, HttpServletRequest httpServletRequest) throws UrlNotFoundException, ServerException {
        
        Method method;
        
        try {
            method = getMethod(instance, urlAnalyzer);
            Object[] parameters = ParameterManager.getParameters(instance, method, httpServletRequest, urlAnalyzer);
            return (Result) method.invoke(instance, parameters);
            
        } catch (NoSuchMethodException | SecurityException e) {
            throw new UrlNotFoundException(
                "Path to \"" +
                urlAnalyzer.getClassName() + "." +
                urlAnalyzer.getMethodName() + "\" was not found or access is not allowed.", e);
        } 
        catch (UnsupportedTypeException | NonMatchingAnnotationsException e) {
            throw new ServerException(
                "Exception on \"" +
                urlAnalyzer.getClassName() + "." +
                urlAnalyzer.getMethodName() + "\": " + 
                e.getMessage(), e);
        }
        catch (ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // InvocationTargetException: Catches everything in case a user's 
            // method throws any kind of exception
            throw new ServerException(
                "Cinnamon captured an unhandled exception in method: \"" +
                urlAnalyzer.getClassName() + "." +
                urlAnalyzer.getMethodName() + "\", details: " + 
                e.getMessage(), e);
        } 
        
    }
    
    
    /**
     * Looks for a method named as urlAnalyzer.getMethod() and
     * returns it.
     * If the target instance contains more than one public method
     * with the required name, this method will return only the first 
     * method found.
     * 
     * From a URL perspective it does not make any sense to have more than
     * one method with the same name. Think about the following case:
     * 
     * methodA(String a) {...}
     * methodA(String name) {...}
     * 
     * What method should we call?
     * 
     * http://domain.com/page/ClassName/methodA
     * 
     * FUTURE: We may add an annotation to accept only requests from either 
     * GET or POST. We can do the validation here.
     * Though not really sure if can be useful:
     * http://stackoverflow.com/questions/24649476/java-servlet-request-getparameter-returns-a-parameter-from-the-query-string
     * 
     * @param instance
     * @param urlAnalyzer
     * @return
     * @throws NoSuchMethodException 
     */
    private static Method getMethod(Controller instance, UrlAnalyzer urlAnalyzer) throws NoSuchMethodException {
        
        Method[] methods = instance.getClass().getMethods();
        
        for (Method method : methods) {
            if (method.getName().equals(urlAnalyzer.getMethodName())
                && method.getReturnType() == Result.class) {
                
                return method;
            }
        }
        
        throw new NoSuchMethodException("Requested method does not exist.");
        
    }
    
}
