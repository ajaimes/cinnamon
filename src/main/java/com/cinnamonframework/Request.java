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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author andres
 * @version 1.3
 */
public class Request {
    
    private Map<String, String[]> parameters;
    private String remoteIp;
    private String referer;
    private UploadedFile[] uploadedFiles;
    private Locale locale;
    private String datePattern = "MM/dd/yyyy";
    private RequestMethod method;
    private String contextPath;
    private String realPath;
    private String uri;
    private String url;
    private String pathInfo;
    private String servletPath;
    private String scheme;
    private String serverName;
    private int serverPort;
    
   
    /**
     * @return the remoteIp
     */
    public String getRemoteIp() {
        return remoteIp;
    }

    /**
     * @param remoteIp the remoteIp to set
     */
    void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    /**
     * @return the referer
     */
    public String getReferer() {
        return referer;
    }

    /**
     * @param referer the referer to set
     */
    void setReferer(String referer) {
        this.referer = referer;
    }

    /**
     * @return the parameters
     */
    public Map<String, String[]> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }
    
    
    /**
     * 
     * @param name
     * @return an BigDecimal value or null if the parameter does not exist or
     *         cannot be cast into an BigDecimal.
     */
    public BigDecimal getAsBigDecimal(String name) {
        
        if (parameters.get(name) != null) {
            BigDecimal bigDecimal = null;
            String value = parameters.get(name)[0];
            
            try {
                bigDecimal = new BigDecimal(value);
                return bigDecimal;
            }
            catch (NumberFormatException e) {}
        }
        
        return null;
    }


    public BigDecimal getAsBigDecimal(String name, BigDecimal defaultValue) {
        
        if (parameters.get(name) != null) {
            BigDecimal bigDecimal = null;
            String value = parameters.get(name)[0];
            
            try {
                bigDecimal = new BigDecimal(value);
                return bigDecimal;
            }
            catch (NumberFormatException e) {}
        }
        
        return defaultValue;
    }


    /**
     * 
     * @param name
     * @return an BigInteger value or null if the parameter does not exist or
     *         cannot be cast into an BigInteger.
     */
    public BigInteger getAsBigInteger(String name) {
        
        if (parameters.get(name) != null) {
            BigInteger bigInteger = null;
            String value = parameters.get(name)[0];
            
            try {
                bigInteger = new BigInteger(value);
                return bigInteger;
            }
            catch (NumberFormatException e) {}
        }
        
        return null;
    }

    
    public BigInteger getAsBigInteger(String name, BigInteger defaultValue) {
        
        if (parameters.get(name) != null) {
            BigInteger bigInteger = null;
            String value = parameters.get(name)[0];
            
            try {
                bigInteger = new BigInteger(value);
                return bigInteger;
            }
            catch (NumberFormatException e) {}
        }
        
        return defaultValue;
    }

    
    /**
     * Returns the boolean value associated with the given name. This function 
     * will return true when the associated value equals "true" or "yes" (case
     * insensitive).
     * 
     * @param name
     * @return true if the associated value equals "true"; false if value
     *   cannot be parsed; null if does not exist.
     */
    public Boolean getAsBoolean(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            return value.equalsIgnoreCase("true") ||
                   value.equalsIgnoreCase("yes");
        }
        else return null;
    }
    

    public boolean getAsBoolean(String name, boolean defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            return value.equalsIgnoreCase("true") ||
                   value.equalsIgnoreCase("yes");
        }
        else return defaultValue;
    }


    public Date getAsDate(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                dateFormat.setLenient(false);
                return dateFormat.parse(value);
            }
            catch (ParseException pe) {}
        }
        
        return null;
    }
    

    public Date getAsDate(String name, Date defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                dateFormat.setLenient(false);
                return dateFormat.parse(value);
            }
            catch (ParseException pe) {}
        }
        
        return defaultValue;
    }


    /**
     * 
     * @param name
     * @return a Double value or null if the parameter does not exist or cannot
     *         be cast into a Double.
     */
    public Double getAsDouble(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Double.parseDouble(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return null;
    }
    
    
    /**
     * 
     * @param name
     * @return a Double value or defaultValue if the parameter does not exist or cannot
     *         be cast into a Double.
     */
    public double getAsDouble(String name, double defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Double.parseDouble(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return defaultValue;
    }


    /**
     * 
     * @param name
     * @return a Float value or null if the parameter does not exist or cannot
     *         be cast into a Float.
     */
    public Float getAsFloat(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Float.parseFloat(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return null;
    }


    /**
     * 
     * @param name
     * @return a Float value or defaultValue if the parameter does not exist or cannot
     *         be cast into a Float.
     */
    public float getAsFloat(String name, float defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Float.parseFloat(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return defaultValue;
    }
    
    
    /**
     * 
     * @param name
     * @return an Integer value or null if the parameter does not exist or cannot
     *         be cast into an Integer.
     */
    public Integer getAsInteger(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Integer.parseInt(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return null;
    }
    
    /**
     * 
     * @param name
     * @return an Integer value or defaultValue if the parameter does not exist 
     *         or cannot be cast into an Integer.
     */
    public int getAsInteger(String name, int defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Integer.parseInt(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return defaultValue;
    }

    
    /**
     * 
     * @param name
     * @return a Long value or null if the parameter does not exist or cannot
     *         be cast into a Long.
     */
    public Long getAsLong(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Long.parseLong(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return null;
    }


    /**
     * 
     * @param name
     * @return a Long value or defaultValue if the parameter does not exist or cannot
     *         be cast into a Long.
     */
    public long getAsLong(String name, long defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Long.parseLong(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return defaultValue;
    }

    
    /**
     * 
     * @param name
     * @return a Short value or null if the parameter does not exist or cannot
     *         be cast into a Short.
     */
    public Short getAsShort(String name) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Short.parseShort(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return null;
    }
    

    /**
     * 
     * @param name
     * @return a Short value or defaultValue if the parameter does not exist or cannot
     *         be cast into a Short.
     */
    public short getAsShort(String name, short defaultValue) {
        if (parameters.get(name) != null) {
            String value = parameters.get(name)[0];
            
            try {
                return Short.parseShort(value);
            }
            catch (NullPointerException | NumberFormatException e) {}
        }
        
        return defaultValue;
    }

    
    /**
     * Returns the string value associated with the given name or null if
     * an item with that name does not exist.
     * Same as getParameter.
     * 
     * @param name the parameter name
     * @return a string value for the given name or null if cannot be found.
     */
    public String getAsString(String name) {
        if (parameters.get(name) != null) {
            return parameters.get(name)[0];
        }
        
        return null;
    }

    
    // TODO: Check this an implement in getAsInteger and so on if the 
    // localization allows it. Some countries use "." as thousand separator.
    // A solution would be to get the thousand separator and remove that.
   /* public String removeCommas(String s) {
        // locale.get the thousand separator
        return s.replaceAll(",", "");
    }*/
    
    
    public String[] getParameterValues(String name) {
        return parameters.get(name);
    }
    
    public UploadedFile getUploadedFile(String name) {
        for (UploadedFile file : uploadedFiles) {
            if (name.equals(file.getName())) {
                return file;
            }
        }
        
        return null;
    }
    
    public UploadedFile getUploadedFile(int id) {
        if (id >= 0 && id < uploadedFiles.length) {
            return uploadedFiles[id];
        }
        
        return null;
    }
    
    public UploadedFile[] getUploadedFiles() {
        return uploadedFiles;
    }
    
    void setUploadedFiles(UploadedFile[] uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * @param locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }
    
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * @return the method
     */
    public RequestMethod getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    void setRequestMethod(RequestMethod method) {
        this.method = method;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * @param contextPath the contextPath to set
     */
    void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * @return the realPath
     */
    public String getRealPath() {
        return realPath;
    }

    /**
     * @param realPath the realPath to set
     */
    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    /**
     * @return the uri
     */
    public String getURI() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * @return the url
     */
    public String getURL() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * @return the pathInfo
     */
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * @param pathInfo the pathInfo to set
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    /**
     * @return the servletPath
     */
    public String getServletPath() {
        return servletPath;
    }

    /**
     * @param servletPath the servletPath to set
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    /**
     * @return the scheme
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * @param scheme the scheme to set
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort the serverPort to set
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
}
