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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
public class Properties {
    
    /* No need to modify anything beyond this point */
    // <editor-fold defaultstate="collapsed" desc="Properties base code">
    
    private final File file;
    private final String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private final String attributeType = "type";
    private final String attributeValue = "value";
    
    /** 
     * Creates a new instance of a Properties class. 
     * This instance will automatically populate its values from the specified
     * xml file.
     * @param fileName The Properties file name with an optional path.
     */    
    public Properties(String fileName) {
        file = new File(fileName);
        load();
    }
    
    /**
     * Looks for the corresponding file name and initializes properties values
     * from it.
     * If the file is not found or any other error happens, then properties
     * will use their default values.
     */
    private void load() {
        
        if (file.exists()) {
            
            try {
                DocumentBuilder db = 
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            
                Document doc = db.parse(file);
                if (doc.hasChildNodes()) {
                    populate(doc.getChildNodes());
                }
            }
            // if there's any problem loading the configuration file, 
            // just go ahead with the default values
            catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(Properties.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        
    }
    
    /**
     * Populates all properties by using reflexion.
     * @param nodeList 
     */
    private void populate(NodeList nodeList) {
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            
            Node node = nodeList.item(i);
            
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                
                Element element = (Element) node;
                String setter = "set" + node.getNodeName();
                String type = element.getAttribute(attributeType);
                String value = element.getAttribute(attributeValue);
                
                // Look for an appropiate setter.
                Method method = findSetter(setter, type);
                if (method != null) {
                    try {
                        // Call the setter.
                        method.setAccessible(true);
                        method.invoke(this, toObject(type, value));
                    }
                    catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        // If it finds a problem, ignore and use the default value
                    }
                }
                
            }
            
        }
        
    }

    /**
     * Uses reflection to find an appropriate method setter.
     * @param setter The setter name to look for, ie: "setId"
     * @param type The type of the setter parameter, ie: "double"
     * @return a Method referencing the setter or null if not found.
     */
    private Method findSetter(String setter, String type) {
        // Get reference to current type
        Class thisClass = this.getClass();
        
        Method method = null;
        
        try {
            switch (type) {
                case "boolean": 
                    method = thisClass.getDeclaredMethod(setter, boolean.class); break;
                case "byte": 
                    method = thisClass.getDeclaredMethod(setter, byte.class); break;            
                case "date": 
                    method = thisClass.getDeclaredMethod(setter, Date.class); break;
                case "double": 
                    method = thisClass.getDeclaredMethod(setter, double.class); break;                
                case "float": 
                    method = thisClass.getDeclaredMethod(setter, float.class); break;
                case "int": 
                    method = thisClass.getDeclaredMethod(setter, int.class); break;
                case "long": 
                    method = thisClass.getDeclaredMethod(setter, long.class); break;
                case "short": 
                    method = thisClass.getDeclaredMethod(setter, short.class); break;
                case "String": 
                    method = thisClass.getDeclaredMethod(setter, String.class); break;
            }
        }
        catch (NoSuchMethodException ex) {
            // If not found, then we'll return null
        }
        
        return method;
    }    
    
    
    /**
     * Turns a String value into the corresponding java type value.
     * @param type the target type.
     * @param value the value to convert.
     * @return an object representation of the value.
     */
    private Object toObject(String type, String value) {
        Object object = null;
        
        try {
            switch (type) {
                case "boolean": 
                    object = Boolean.parseBoolean(value); break;
                case "byte": 
                    object = Byte.parseByte(value); break;
                case "date": 
                    object = new SimpleDateFormat(dateFormat).parse(value); break;
                case "double": 
                    object = Double.parseDouble(value); break;
                case "float": 
                    object = Float.parseFloat(value); break;
                case "int":
                    object = Integer.parseInt(value); break;
                case "long": 
                    object = Long.parseLong(value); break;
                case "short": 
                    object = Short.parseShort(value); break;
                case "String": 
                    object = value;
            }
        }
        catch (NumberFormatException | ParseException ex) {
            // If not found, then we'll return null
        }
        
        return object;
    }
    
    
    /**
     * Saves all class properties to a file. The file name to use is the one
     * specified in the constructor.
     */
    public void save() {
        try {
            DocumentBuilder db = 
                DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document doc = db.newDocument();
            Element root = doc.createElement("properties");
            
            // Get reference to current type
            Class thisClass = this.getClass();
            
            // Iterate the getters to get the properties to save.
            Method[] getters = thisClass.getDeclaredMethods();
            for (Method getter : getters) {
                if (isGetter(getter)) {
                    getter.setAccessible(true);
                    
                    Element element = doc.createElement("");
                    element.setAttribute(attributeType, getter.getReturnType().getName());
                    element.setAttribute(attributeValue, resultToString(getter));
                    root.appendChild(element);
                }
            }
            
            saveToFile(doc);
            
        }
        catch (ParserConfigurationException | DOMException | SecurityException ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Calls the given getter and converts its return value into a string.
     * @param getter The method from which the value will be obtained.
     * @return a string representation of the value obtained from the getter
     *         or an empty string if something goes wrong.
     */
    public String resultToString(Method getter) {
        
        String s = "";
        
        try {            
            getter.setAccessible(true);
            
            switch (getter.getReturnType().getName()) {
                case "Date": 
                    s = new SimpleDateFormat(dateFormat).format(
                        (Date) getter.invoke(this)); break;
                default: 
                    s = getter.invoke(this).toString();
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // return an empty string...
        }
        
        return s;
    }
    
    
    /** 
     * Performs the actual write of the file to disk.
     * @param doc the in-memory xml document to save.
     */
    private void saveToFile(Document doc) {
        try {
            // Prepare to write the content into a file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            
            // set options for the resulting file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            // and save it...
            transformer.transform(source, result);
        } catch (IllegalArgumentException | TransformerException ex) {
            Logger.getLogger(Properties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * Checks if a given method is a getter.
     * @param method the method to check.
     * @return true if it is a getter, false otherwise.
     */
    private boolean isGetter(Method method){
        if (!method.getName().startsWith("get")) return false;
        if (method.getParameterTypes().length != 0) return false;  
        if (void.class.equals(method.getReturnType())) return false;
        return true;
    }
            
    // </editor-fold>
    
}
