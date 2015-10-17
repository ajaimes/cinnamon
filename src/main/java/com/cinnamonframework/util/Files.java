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
package com.cinnamonframework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
public class Files {
    
    
    /**
     * Returns a file extension or an empty string if the file does not
     * have one.
     * @param fileName
     * @return a file extension
     */
    public static String getExtension(String fileName) {
        if (fileName == null) 
            throw new NullPointerException("fileName cannot be null.");
        
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && fileName.length() > pos + 1) {
            return fileName.substring(pos + 1);
        }
        return "";
    }
    
    
    /**
     * Returns a file name without extension.
     * @param fileName
     * @return a file name without extension.
     */
    public static String removeExtension(String fileName) {
        if (fileName == null) 
            throw new NullPointerException("fileName cannot be null.");
        
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            return fileName.substring(0, pos);
        }
        return fileName;
    }
    
    
    /**
     * Returns a file's md5 hash.
     * Based on the work of Mkyong.
     * @see http://www.mkyong.com/java/java-md5-hashing-example/
     * @param file
     * @return a file's md5 hash
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static String md5(File file) 
        throws FileNotFoundException, IOException {
        
        if (file == null) 
            throw new NullPointerException("file cannot be null.");
        
        StringBuilder sb = new StringBuilder();
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);

            byte[] dataBytes = new byte[1024];

            int nread; 
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();

            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
        }
        catch (NoSuchAlgorithmException e) {
            Logger.getLogger(Strings.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return sb.toString();
    }
    
}
