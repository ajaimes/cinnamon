/* 
 * Cinnamon Framework
 * Copyright (c) 2014, Andres Jaimes
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author andres
 */
public class Strings {

    
    /**
     * Surrounds a substring with &lt;strong&gt; tags. For example, if the
     * original string is: "The brown fox jumps over the lazy dog", and the
     * substring is "Jumps", then the resulting string is going to be: "The
     * brown fox &lt;strong&gt;jumps&lt;/strong&gt; over the lazy dog".
     *
     * You have to notice that this function is case insensitive and the
     * returning string will follow the original string's case sensitivity.
     *
     * @param src The original string.
     * @param what The substring to look for.
     * @return A string with a substring surrounded with &lt;strong&gt; tags.
     */
    /*public static String emphasize(String src, String what) {
        return emphasize(src, what, null);
    }*/
    
    
    /**
     * Surrounds a substring with &lt;strong&gt; tags. For example, if the
     * original string is: "The brown fox jumps over the lazy dog", and the
     * substring is "Jumps", then the resulting string is going to be: "The
     * brown fox &lt;strong&gt;jumps&lt;/strong&gt; over the lazy dog".
     *
     * You have to notice that this function is case insensitive and the
     * returning string will follow the original string's case sensitivity.
     *
     * @param src The original string.
     * @param what The substring to look for.
     * @param cssClass An optional css class for the matching strings.
     * @return A string with a substring surrounded with &lt;strong&gt; tags.
     */
    /*public static String emphasize(String src, String what, String cssClass) {

        if (src != null && what != null) {
            // look for the substring
            int i = src.toLowerCase().indexOf(what.toLowerCase());

            // if you find it
            if (i >= 0) {
                StringBuilder sb = new StringBuilder(src);
                String s = sb.substring(i, i + what.length());
                
                if (cssClass == null) {
                    return sb.replace(i, i + what.length(), "<strong>" + s + "</strong>").toString();
                }
                else {
                    return sb.replace(i, i + what.length(), "<strong class=\"" + cssClass + "\">" + s + "</strong>").toString();
                }
            } // if not found, return the original string
            else {
                return src;
            }
        }

        return "";
    }*/
    
    
    /**
     * Returns true if the given string is null or an empty string.
     * @param s the string to check.
     * @return true if the string is null or empty.
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    
    /**
     * Returns true if the given string is null or contains only
     * white-space characters.
     * @param s the string to check.
     * @return true if the given string is null or contains only
     *         white-space characters.
     */
    public static boolean isNullOrBlank(String s) {
        return s == null || s.trim().length() == 0;
    }
    

    /**
     * Joins a list of strings using a delimeter.
     * @param list
     * @param delim
     * @return a string including all the elements separated by "delim"
     */
    public static String join(List<String> list, String delim) {

        if (list == null || delim == null)
            throw new NullPointerException("Parameter cannot be null.");
        
        String[] array = new String[list.size()];
        array = list.toArray(array);

        return join(array, delim);

    }

    /**
     * Joins a list of strings using delim.
     * @param list
     * @param delim
     * @return a string including all the elements separated by "delim"
     */
    public static String join(String[] list, String delim) {

        if (list == null || delim == null)
            throw new NullPointerException("Parameter cannot be null.");
        
        StringBuilder sb = new StringBuilder();
        String loopDelim = "";

        for (String s : list) {
            sb.append(loopDelim).append(s);
            loopDelim = delim;
        }

        return sb.toString();
    }

    
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    /**
     * Converts the given string to its slug representation. For example, for
     * "Abc and Ábc" it returns "abc-and-abc".
     *
     * @param s the string to process
     * @return a slug representation
     */
    public static String toSlug(String s) {
        if (s == null)
            throw new NullPointerException("Parameter cannot be null.");

        String noWhiteSpace = WHITESPACE.matcher(s).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }
    
    
    /**
     * Returns a string's md5 hash.
     * Based on the work of Mkyong.
     * @see http://www.mkyong.com/java/java-md5-hashing-example/
     * @return a string's md5 hash.
     */
    public static String md5(String s) {
        if (s == null)
            throw new NullPointerException("Parameter cannot be null.");
        
        MessageDigest md;
        StringBuilder sb = new StringBuilder();
        
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
        
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(Strings.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        
        return sb.toString();
    }

}
