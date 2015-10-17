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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Session map representation for values that will persist during requests.
 * 
 * A session might be empty in the following cases:
 * - When user calls the clear method.
 * - When user calls the invalidate method. 
 * - When the corresponding HttpSession has expired.
 * 
 * @author Andres Jaimes Velazquez (http://andres.jaimes.net)
 */
public class Session implements Map<String, Object> {
    
    private String id;
    private long creationTime;
    private long lastAccessedTime;
    private int maxInactiveInterval;
    private boolean active = false;
    private boolean invalid = false;
    private final Map<String,Object> attributes = new HashMap<>();
    
    
    /**
     * Returns a string containing the unique identifier assigned to this session.
     * @return 
     */
    public String getId() {
        return id;
    }
    
    void setId(String id) {
        this.id = id;
    }
    
    
    /**
     * Returns the last time the client sent a request associated with this 
     * session, as the number of milliseconds since 
     * midnight January 1, 1970 GMT, and marked by the time the container 
     * received the request.
     * @return the lastAccessedTime
     */
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * @param lastAccessedTime the lastAccessedTime to set
     */
    void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    /**
     * Returns the time when this session was created, measured in milliseconds 
     * since midnight January 1, 1970 GMT.
     * @return the creationTime
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Returns the maximum time interval, in seconds, that the servlet container 
     * will keep this session open between client accesses.
     * @return 
     */
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }
    
    void setMaxInactiveInteraval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }
    
    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Removes the current HttpSession object associated to this session.
     * Once a session is invalidated, it should not be used any more.
     * 
     * To create a new session, just add new elements into session and it will
     * automatically create the corresponding HttpSession instance.
     * The actual HttpSession invalidation takes place in 
     * SessionManager.sessionToHttpSession.
     * 
     * @see com.cinnamon.SessionManager.sessionToHttpSession
     */
    public void invalidate() {
        invalid = true;
        attributes.clear(); // remove all attributes
        id = null;
        creationTime = 0L;
        lastAccessedTime = 0L;
    }
    
    public boolean isInvalid() {
        return invalid;
    }
    
    
    /* Map methods */
    
    @Override
    public void clear() {
        attributes.clear();
    }
    
    @Override
    public int size() {
        return attributes.size();
    }

    @Override
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return attributes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return attributes.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return attributes.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return attributes.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return attributes.remove(key);
    }

    @Override
    public void putAll(Map m) {
        attributes.putAll(m);
    }

    @Override
    public Set<String> keySet() {
        return attributes.keySet();
    }

    @Override
    public Collection<Object> values() {
        return attributes.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return attributes.entrySet();
    }


    
}
