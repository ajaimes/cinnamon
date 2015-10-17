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

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author andres
 */
class RequestManager {
    
    static Request createRequest(HttpServletRequest httpServletRequest, RequestMethod requestMethod) {
        Request request = new Request();
        
        request.setRequestMethod(requestMethod);
        request.setReferer(httpServletRequest.getHeader("referer"));
        request.setRemoteIp(getRemoteAddr(httpServletRequest));
        request.setParameters(httpServletRequest.getParameterMap());
        request.setLocale(httpServletRequest.getLocale());
        request.setContextPath(httpServletRequest.getContextPath());
        request.setRealPath(httpServletRequest.getServletContext().getRealPath("/"));
        request.setURI(httpServletRequest.getRequestURI());
        request.setURL(httpServletRequest.getRequestURL().toString());
        request.setPathInfo(httpServletRequest.getPathInfo());
        request.setServletPath(httpServletRequest.getServletPath());
        request.setScheme(httpServletRequest.getScheme());
        request.setServerName(httpServletRequest.getServerName());
        request.setServerPort(httpServletRequest.getServerPort());
        //request.setUploadedFiles(processUploadedFiles(httpServletRequest, bundle));
	
        return request;        
    }   
    
    
    /**
     * Gets the remote IP address. This method tries to guess the IP address
     * even if the client is behind a firewall.
     *
     * @param httpServletRequest the HTTP request.
     * @return the IP address of the client.
     */
    private static String getRemoteAddr(HttpServletRequest httpServletRequest) {

        String ip = httpServletRequest.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_X_FORWARDED");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_X_CLUSTER_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_FORWARDED");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("HTTP_VIA");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getHeader("REMOTE_ADDR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = httpServletRequest.getRemoteAddr();  
        }
        // look for multiple IP's and get the first one.
        if (ip != null && ip.indexOf(',') >= 0) {
            ip = ip.substring(0, ip.indexOf(','));
        }
        return ip;  
    }
    
    
}
