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

import com.cinnamonframework.util.Strings;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
public class Result {


    /**
     * Content Types available to cinnamon.
     */
    class ContentType {
        public static final String Json = "application/json; charset=utf-8";
        public static final String TextHtml = "text/html; charset=utf-8";
        public static final String TextPlain = "text/plain; charset=utf-8";
        public static final String Xml = "application/xml; charset=utf-8";
    }
    
    /**
     * Default HTTP Status Codes.
     */
    class StatusCode {
        public static final int Continue = 100;
        public static final int SwitchingProtocols = 101;
        public static final int Ok = 200;
        public static final int Created = 201;
        public static final int Accepted = 202;
        public static final int NonAuthoritativeInformation = 203;
        public static final int NoContent = 204;
        public static final int ResetContent = 205;
        public static final int PartialContent = 206;
        public static final int MultipleChoices = 300;
        public static final int MovedPermanently = 301;
        public static final int Found = 302;
        public static final int SeeOther = 303;
        public static final int NotModified = 304;
        public static final int UseProxy = 305;
        public static final int TemporaryRedirect = 307;
        public static final int BadRequest = 400;
        public static final int Unauthorized = 401;
        public static final int PaymentRequired = 402;
        public static final int Forbidden = 403;
        public static final int NotFound = 404;
        public static final int MethodNotAllowed = 405;
        public static final int NotAcceptable = 406;
        public static final int ProxyAuthenticationRequired = 407;
        public static final int RequestTimeout = 408;
        public static final int Conflict = 409;
        public static final int Gone = 410;
        public static final int LengthRequired = 411;
        public static final int PreconditionFailed = 412;
        public static final int RequestEntityTooLarge = 413;
        public static final int RequestURITooLong = 414;
        public static final int UnsupportedMediaType = 415;
        public static final int RequestedRangeNotSatisfiable = 416;
        public static final int ExpectationFailed = 417;
        public static final int InternalError = 500;
        public static final int NotImplemented = 501;
        public static final int BadGateway = 502;
        public static final int ServiceUnavailable = 503;
        public static final int GatewayTimeout = 504;
        public static final int HTTPVersionNotSupported = 505;
    }
    
    private String contentType = ContentType.TextHtml;
    private int statusCode = StatusCode.Ok;
    private String jsp = null;
    private String redirect = null;
    private String content = null;
    
   
    /**
     * Returns true if this result should perform a redirect.
     * @return true if this result should perform a redirect.
     */
    public boolean isRedirect() {
        return redirect != null && redirect.length() > 0;
    }
    
    /**
     * Returns true if this result should load a JSP.
     * @return true if this result should load a JSP.
     */
    public boolean isJsp() {
        return jsp != null && jsp.length() > 0;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public String getJsp() {
        return jsp;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getRedirect() {
        return redirect;
    }
    
    /**
     * @param contentType the contentType to set
     * @throws NullPointerException if parameter is null or blank.
     */
    public void setContentType(String contentType) {
        if (Strings.isNullOrBlank(contentType)) 
            throw new NullPointerException("Parameter cannot be null or blank.");
        
        this.contentType = contentType;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @param jsp the jsp to set
     * @throws NullPointerException if parameter is null or blank.
     */
    public void setJsp(String jsp) {
        if (Strings.isNullOrBlank(jsp)) 
            throw new NullPointerException("Parameter cannot be null or blank.");
        
        this.jsp = jsp;
    }

    /**
     * @param redirect the redirect to set
     * @throws NullPointerException if parameter is null or blank.
     */
    public void setRedirect(String redirect) {
        if (Strings.isNullOrBlank(redirect)) 
            throw new NullPointerException("Parameter cannot be null or blank.");

        this.redirect = redirect;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     * @throws NullPointerException if parameter is null.
     */
    public void setContent(String content) {
        if (content == null)
            throw new NullPointerException("Parameter cannot be null.");
        
        this.content = content;
    }
    
    
}
