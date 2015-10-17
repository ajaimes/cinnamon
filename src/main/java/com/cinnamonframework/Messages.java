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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
public class Messages implements Map<String, String> {
    
    private final Map<String, String> messages = new HashMap<>();
    private ResourceBundle bundle = null;
    
    Messages() {
        
    }
    
    Messages(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public void addMessage(String key, String message) {
        messages.put(key, message);
    }
    
    public void addLocalizedMessage(String key, String message) {
        if (bundle != null) {
            messages.put(key, bundle.getString(message));
        }
        else {
            messages.put(key, message);
        }
    }
    
    public String getMessage(String key) {
        return messages.get(key);
    }
    
    public Collection<String> getMessages() {
        return messages.values();
    }
    
    public String getMessages(String openTag, String closeTag) {
        StringBuilder sb = new StringBuilder();
        
        for(String s : messages.values()) {
            sb.append(openTag).append(s).append(closeTag);
        }
        
        return sb.toString();
    }
    
    @Override
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public boolean containsKey(Object key) {
        return messages.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return messages.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return messages.get(key);
    }

    @Override
    public String put(String key, String value) {
        return messages.put(key, key);
    }

    @Override
    public String remove(Object key) {
        return messages.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        messages.putAll(m);
    }

    @Override
    public void clear() {
        messages.clear();
    }

    @Override
    public Set<String> keySet() {
        return messages.keySet();
    }

    @Override
    public Collection<String> values() {
        return messages.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return messages.entrySet();
    }
    
    
}
