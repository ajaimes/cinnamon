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

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Andres Jaimes Velazquez (http://andres.jaimes.net)
 */
class SessionManager {

    static Session createSession(HttpServletRequest httpServletRequest) {
        
        Session session = new Session();
        HttpSession httpSession = httpServletRequest.getSession(false);
    
        if (httpSession != null && httpServletRequest.isRequestedSessionIdValid()) {
            // Get all attributes from session into a map
            for (Enumeration<String> e = httpSession.getAttributeNames(); e.hasMoreElements(); ) {
                String name = e.nextElement();
                session.put(name, httpSession.getAttribute(name));
            }

            session.setId(httpSession.getId());
            session.setCreationTime(httpSession.getCreationTime());
            session.setLastAccessedTime(httpSession.getLastAccessedTime());
            session.setMaxInactiveInteraval(httpSession.getMaxInactiveInterval());
            session.setActive(true);
        }
        
        return session;
    }
    
    /**
     * Takes all the information from Session into a HttpSession.
     * If session is empty, then no httpSession is created.
     * @param session
     * @param httpServletRequest 
     */
    static void sessionToHttpSession(Session session, HttpServletRequest httpServletRequest) {
        
        HttpSession httpSession = httpServletRequest.getSession(false);
        
        // We have 4 cases:
        // A) session is empty
        // B) session is not empty
        // C) httpSession exists
        // D) httpSession does not exist
        
        // And 4 possible combinations:
        
        // A and C
        if (session.isEmpty() && httpSession != null && httpServletRequest.isRequestedSessionIdValid()) {
            emptyHttpSession(httpSession);
            
            // Special case, invalidate httpSession if we're asked to do so:
            // session is empty and invalid, and httpSession is not null
            if (session.isInvalid()) {
                httpSession.invalidate();
            }
        }
        // A and D
        else if (session.isEmpty() && httpSession == null) {
            // nothing happens.
        }
        // B and C
        else if (!session.isEmpty() && httpSession != null && httpServletRequest.isRequestedSessionIdValid()) {
            emptyHttpSession(httpSession);
            copySessionIntoHttpSession(session, httpSession);
        }
        // B and D
        else if (!session.isEmpty() && httpSession == null) {
            httpSession = httpServletRequest.getSession(true);
            copySessionIntoHttpSession(session, httpSession);
        }
        
    }
    
    /**
     * Removes all items from the httpSession.
     * @param httpSession 
     */
    private static void emptyHttpSession(HttpSession httpSession) {
        // Remove all attributes from session object
        for (Enumeration<String> e = httpSession.getAttributeNames(); e.hasMoreElements(); ) {
            httpSession.removeAttribute(e.nextElement());
        }  
    }
    
    /**
     * Copies all items from session into httpSession.
     * @param session
     * @param httpSession 
     */
    private static void copySessionIntoHttpSession(Session session, HttpSession httpSession) {
        // Add the new set of attributes
        for (String s : session.keySet()) {
            httpSession.setAttribute(s, session.get(s));
        }
    }
    
}
