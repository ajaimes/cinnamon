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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 * @version 1.3
 */
public abstract class Controller {
    
    protected Messages messages = null;
    protected Request request = null;
    protected Session session = null;
    protected Map<String, Object> model = new HashMap<>();
    
    final void setMessages(Messages messages) {
        this.messages = messages;
    }
    
    final void setRequest(Request request) {
        this.request = request;
    }
    
    final void setSession(Session session) {
        this.session = session;
    }
    
    protected Result custom(String text, String contentType) {
        Result result = new Result();
        result.setContent(text);
        result.setContentType(contentType);
        return result;
    }
    
    // TODO: 
    /*
    protected Result custom(OutputStream stream, String contentType) {
        return new Result(stream, contentType);
    }*/
    

    protected Result html(String html) {
        Result result = new Result();
        result.setContent(html);
        result.setContentType(Result.ContentType.TextHtml);
        return result;
    }
    
    protected Result jsp(String jsp) {
        Result result = new Result();
        result.setJsp(jsp);
        result.setContentType(Result.ContentType.TextHtml);
        return result;
    }
    
    protected Result jsp(String jsp, String contentType) {
        Result result = new Result();
        result.setJsp(jsp);
        result.setContentType(contentType);
        return result;
    }
    
    protected Result json(String json) {
        Result result = new Result();
        result.setContent(json);
        result.setContentType(Result.ContentType.Json);
        return result;
    }
    
    protected Result redirect(String redirect) {
        Result result = new Result();
        result.setRedirect(redirect);
        result.setStatusCode(Result.StatusCode.SeeOther);
        return result;
    }
    
    protected Result redirect(String redirect, int statusCode) {
        Result result = new Result();
        result.setRedirect(redirect);
        result.setStatusCode(statusCode);
        return result;
    }
    
    protected Result text(String text) {
        Result result = new Result();
        result.setContent(text);
        result.setContentType(Result.ContentType.TextPlain);
        return result;
    }
    
    protected Result xml(String xml) {
        Result result = new Result();
        result.setContent(xml);
        result.setContentType(Result.ContentType.Xml);
        return result;
    }
    
}
