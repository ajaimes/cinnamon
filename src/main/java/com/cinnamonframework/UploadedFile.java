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


/**
 *
 * @author Andres Jaimes Velazquez (http://andres.jaimes.net)
 */
public class UploadedFile {
    
    private String name; // the HTML form input name
    private String submittedFileName;
    private String contentType;
    private long size;
    private String fileName;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the size
     */
    public String getSizeHumanReadable() {
        if (size < 1024) return getSizeInBytes() + " bytes";
        if (size > 1024 && size < 1024*1024) return getSizeInKb() + " Kb";
        if (size > 1024*1024 && size < 1024*1024*1024) return getSizeInMb() + " Mb";
        if (size > 1024*1024*1024 && size < 1024*1024*1024*1024) return getSizeInGb() + " Gb";
        return getSizeInTb() + " Tb";
    }

    /**
     * @return the size
     */
    public long getSizeInBytes() {
        return size;
    }
    
    /**
     * @return the size
     */
    public long getSizeInKb() {
        return size / 1024;
    }
    
    /**
     * @return the size
     */
    public long getSizeInMb() {
        return size / 1024 / 1024;
    }
    
    /**
     * @return the size
     */
    public long getSizeInGb() {
        return size / 1024 / 1024 / 1024;
    }

    /**
     * @return the size
     */
    public long getSizeInTb() {
        return size / 1024 / 1024 / 1024 / 1024;
    }
    
    /**
     * @param size the size to set
     */
    void setSize(long size) {
        this.size = size;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the submittedFileName
     */
    public String getSubmittedFileName() {
        return submittedFileName;
    }

    /**
     * @param submittedFileName the submittedFileName to set
     */
    void setSubmittedFileName(String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    
    void moveTo(String targetDir) {
        
    }
    
    void rename(String name) {
        
    }
    
    void delete() {
        
    }
        
}
