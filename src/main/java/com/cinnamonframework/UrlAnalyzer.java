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

import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
class UrlAnalyzer {
    
    private String className;
    private String methodName = "index"; // default method name
    private String[] parameters;

    /**
     * Example pathInfo:
     * 
     * /HelloWorld/greet
     * 
     * @param pathInfo
     * @throws ServerException 
     */
    UrlAnalyzer(HttpServletRequest httpServletRequest, boolean useSlugs) throws ServerException {
        
        if (httpServletRequest == null) {
            throw new ServerException(
                "UrlAnalyzer: Cannot split URL information. PATH_INFO is null.");
        }
        
        // This will not really work if user configures a url pattern = "/".
        // getServletPath() returns an erroneous response, or perhaps, that's
        // the way it should work. It's always required to specify a url pattern
        // in web.xml or any other web-fragment.xml. Further reading:
        // http://stackoverflow.com/questions/4140448/difference-between-and-in-servlet-mapping-url-pattern
        // http://stackoverflow.com/questions/870150/how-to-access-static-resources-when-mapping-a-global-front-controller-servlet-on
        String pathInfo = httpServletRequest.getRequestURI().substring(
                httpServletRequest.getContextPath().length()
              + httpServletRequest.getServletPath().length());
        String[] array = pathInfo.split("/");

        // Get the class name and the method name.
        // Since we split using the "/" character, then for the following string:
        // "/HelloWorld/greet", we get an array size of 3, where:
        // array[0] = ""
        // array[1] = "hello-world"
        // array[2] = "greet"
        if (array.length< 2) {
            throw new UrlNotFoundException(
                "UrlAnalyzer: Class name was not found in URL.");
        }
        
        int i = 0;
        List<String> list = new LinkedList<>();
        for (String s : array) {
            switch (i) {
                case 1:
                    className = useSlugs ? parseClassName(array[i]) : array[i];
                    break;
                case 2:
                    methodName = useSlugs ? parseMethodName(array[i]) : array[i];
                    break;
                default:
                    if (s.length() > 0) {
                        list.add(s);
                    }
            }
            i++;
        }
        
        parameters = new String[list.size()];
        parameters = list.toArray(parameters);

    }
    
    
    /**
     * Converts a slug into a class name. The convention used is:
     * <ul>
     * <li>"hello-world" turns into "HelloWorld"</li>
     * <li>"admin-user-interface" turns into "AdminUserInterface"</li>
     * <li>"a" turns into "A"</li>
     * </ul>
     * 
     * @param slug
     * @return 
     */
    private String parseClassName(String slug) {
        String[] parts = slug.split("-");
        StringBuilder sb = new StringBuilder();
        for (String s : parts) {
            if (s.length() > 0) {
                sb.append(s.substring(0, 1).toUpperCase());
            }
            if (s.length() > 1) {
                sb.append(s.substring(1));
            }
        }
        
        return sb.toString();
    }
    
    
    
    /**
     * Converts a slug into a method name. The convention used is:
     * <ul>
     * <li>"hello-world" turns into "helloWorld"</li>
     * <li>"admin-user-interface" turns into "adminUserInterface"</li>
     * <li>"a" remains the same</li>
     * </ul>
     * 
     * @param slug
     * @return 
     */
    private String parseMethodName(String slug) {
        String[] parts = slug.split("-");
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String s : parts) {
            if (i++ == 0) {
                sb.append(s);
            }
            else {
                if (s.length() > 0) {
                    sb.append(s.substring(0, 1).toUpperCase());
                }
                if (s.length() > 1) {
                    sb.append(s.substring(1));
                }
            }
        }
        
        return sb.toString();
    }
    
    
    /**
     * @return the className
     */
    String getClassName() {
        return className;
    }

    /**
     * @return the methodName
     */
    String getMethodName() {
        return methodName;
    }

    /**
     * @return the parameters
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }
    
}
