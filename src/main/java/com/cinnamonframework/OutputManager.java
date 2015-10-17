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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author andres
 */
class OutputManager {
    
    static final String JspDirectory = "/WEB-INF/jsp/";
    static final String JspExtension = ".jsp";
    
    static void processOutput(Result result, Controller controller,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServerException {
        
        // Save session values
        SessionManager.sessionToHttpSession(controller.session, httpServletRequest);
        
        // Perform a redirection
        if (result.isRedirect()) {
            httpServletResponse.setStatus(result.getStatusCode());
            httpServletResponse.addHeader("Location", result.getRedirect());
        }
        
        
        // Use a jsp view to output content
        else if (result.isJsp()) {
            try {
                httpServletResponse.setContentType(result.getContentType());
                httpServletRequest.setAttribute("request", controller.request);
                httpServletRequest.setAttribute("session", controller.session);
                httpServletRequest.setAttribute("messages", controller.messages);
                for (Map.Entry<String, Object> entry : controller.model.entrySet()) {
                    if (!entry.getKey().equals("request") && !entry.getKey().equals("session") && !entry.getKey().equals("messages")) {
                        httpServletRequest.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
                httpServletRequest.getRequestDispatcher(
                        JspDirectory + result.getJsp() + JspExtension).forward(httpServletRequest, httpServletResponse);
                
            } catch (ServletException | IOException ex) {
                throw new ServerException(ex);
            }
            
        }
        
        // Write content directly 
        else {
            httpServletResponse.setContentType(result.getContentType());
            
            try {
                try (PrintWriter out = httpServletResponse.getWriter()) {
                    out.print(result.getContent());
                    out.flush();
                }                
            } catch (IOException ex) {
                throw new ServerException(ex);
            }
            
        }
        
    }
    
    
}
