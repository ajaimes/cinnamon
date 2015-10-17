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
package com.cinnamonframework;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Andres Jaimes (http://andres.jaimes.net)
 */
public class CinnamonServlet extends HttpServlet {

    //private String contextDir = null;
    private static final String logger = CinnamonServlet.class.getName();
    private static final String initParameterControllers = "com.cinammonframework.controllers";
    private String controllersPackage;
    private static final String initParameterUseSlugs = "com.cinammonframework.use-slugs";
    private boolean useSlugs;
    

    /**
     * Initializes the main controller. 
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        // Try to get the controller information from the application web.xml file,
        controllersPackage = getServletContext().getInitParameter(initParameterControllers);
        // if not found, use the default controller's package name.
        if (controllersPackage == null) {
            controllersPackage = getServletConfig().getInitParameter(initParameterControllers);
        }
        
        // Try to get the controller information from the application web.xml file,
        if (getServletContext().getInitParameter(initParameterUseSlugs) != null) {
            useSlugs = Boolean.parseBoolean(getServletContext().getInitParameter(initParameterUseSlugs));
        }
        // if not found, use the default controller's package name.
        else {
            useSlugs = Boolean.parseBoolean(getServletConfig().getInitParameter(initParameterUseSlugs));
        }
        
    }
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param requestMethod the httpServletRequest method
     * @param httpServletRequest servlet httpServletRequest
     * @param httpServletResponse servlet httpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(RequestMethod requestMethod,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {

        try {
            long startTime = System.nanoTime();
            
            UrlAnalyzer urlAnalyzer = new UrlAnalyzer(httpServletRequest, useSlugs);
            Controller controller = ControllerManager.instantiateController(
                    controllersPackage + "." + urlAnalyzer.getClassName());
            
            if (controller instanceof HttpServletRequestAware)
                ((HttpServletRequestAware) controller).setHttpServletRequest(httpServletRequest);
            if (controller instanceof HttpServletResponseAware)
                ((HttpServletResponseAware) controller).setHttpServletResponse(httpServletResponse);
            
            controller.setRequest(
                RequestManager.createRequest(httpServletRequest, requestMethod));
            controller.setMessages(new Messages()); // TODO: add bundle?
            controller.setSession(
                SessionManager.createSession(httpServletRequest));
            Result result = ControllerManager.invoke(controller, urlAnalyzer, httpServletRequest);
            
            if (result != null) {
                OutputManager.processOutput(result, (Controller) controller, 
                        httpServletRequest, httpServletResponse);
            }
            else {
                throw new ServerException("CinnamonServlet: Response from " +
                    urlAnalyzer.getClassName() + "." +
                    urlAnalyzer.getMethodName() + " is null.");
            }
            
            long endTime = System.nanoTime();
            
            // TODO: delete the performance code 
            Object[] params = {
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()),
                urlAnalyzer.getClassName() + "." + urlAnalyzer.getMethodName(),
                "" + ((endTime - startTime) / 1_000_000),
                "" + Runtime.getRuntime().totalMemory(),
                "" + Runtime.getRuntime().freeMemory()
            };
            Logger.getLogger(logger).log(Level.INFO,
                    "{0}\tCtrl: {1}\tExecTime:{2}ms\tTotalMem:{3}\tFreeMem:{4}",
                    params);
        }
        
        catch (UrlNotFoundException e) {
            // Send a 404 Not Found if ControllerManager.instantiateController cannot find
            // the right class
            Logger.getLogger(logger).log(Level.WARNING, e.getMessage(), e);
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND, "Please check the server log for details.");
        }
        catch (ServerException e) {
            // Send a 500 Internal Server Error
            Logger.getLogger(logger).log(Level.SEVERE, e.getMessage(), e);
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Please check the server log for details.");
        }
        

    }

    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet result
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(RequestMethod.Get, request, response);
    }

    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet result
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(RequestMethod.Post, request, response);
    }

    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Cinnamon Servlet Controller, fast web development using Java.";
    }
    
    
    /**
     * Processes all uploaded files. This method looks for uploaded files in 
     * the request, populates the related classes and saves the file into the
     * defined uploads directory. 
     * 
     * If the uploaded file is a supported image type, it also resizes it to
     * get three common file sizes.
     * 
     * @param httpServletRequest
     * @param bundle
     * @return an empty array if no files were found
     * @throws ServerException
     *//*
    public UploadedFile[] processUploadedFiles(HttpServletRequest httpServletRequest, ResourceBundle bundle)
            throws ServerException {

        List<UploadedFile> files = new ArrayList();

        try {
            for (Part part : httpServletRequest.getParts()) {
                
                if (part.getContentType() != null && !isAllowedContentType(part.getContentType())) {
                    throw new ServerException(bundle.getString("exception.upload.fileTypeNotAllowed") + part.getSubmittedFileName());
                }
                
                // The developer may check if there are files uploaded; if 
                // there are no, then he can send a message to the user telling
                // him that something went wrong and that he should check for
                // conditions like file size and file type.
                if (part.getContentType() != null && part.getSize() > 0) {
                
                    UploadedFile file = new UploadedFile();
                    file.setName(part.getName());
                    file.setSubmittedFileName(part.getSubmittedFileName());
                    file.setSize(part.getSize());
                    file.setContentType(part.getContentType());
                    
                    String tempName = assignNameForUploadedFile(file);                    
                    saveUploadedFile(File.separator + tempName, part);
                    file.setFileName(tempName);
                    
                    files.add(file);
                    
                    // If everything went ok, then process the resized versions...
                    if (ImageUtil.isImage(getContextDir() + tempName)) {
                        try {
                            ImageUtil.resize(getContextDir() + tempName);
                        }
                        catch (ServerException e) {
                            // If cannot resize the picture, report it to admin 
                            // and silently move on.
                            // One possible case for this exception would be if 
                            // we receive a file that has an extension JPG but 
                            // is a different type of file, like a PDF.
                            Logger.getLogger(CinnamonServlet.class.getName()).log(Level.WARNING, e.getMessage());
                        }
                    }
                }
            }
        }
        // Thrown if file or request size exceed the maximum limits allowed
        catch (IllegalStateException e) {
            throw new ServerException(bundle.getString("exception.upload.fileMaxSize"));
        }
        // Thrown by the saveUploadedFile function
        catch (IOException e) {
            throw new ServerException(bundle.getString("exception.upload.fileCannotBeSaved"));
        }
        // Thrown if this is not a multipart request. We just ignore it.
        catch (ServletException e) {
        }
        
        UploadedFile[] uploadedFiles = new UploadedFile[files.size()];
        uploadedFiles = files.toArray(uploadedFiles);
        return uploadedFiles;
        
    }*/
    
    
    /**
     * Uses the uploaded file's information and the current file names in 
     * the server to assign a suitable name for the recently uploaded file.
     * The server will try to use the submittedFileName by removing non-safe web
     * characters and by shortening it.
     * 
     * Once it has a candidate name, it will look in the current uploads 
     * directory if there's a file with that name. If there's one already, it
     * will append a numeric suffix to the name and will look again for 
     * a file with that name. 
     * 
     * The process will go on until a suitable file name is found.
     * 
     * @param file
     * @return 
     *//*
    private String assignNameForUploadedFile(UploadedFile file) {
        
        String tempName;
        
        Calendar calendar = new GregorianCalendar();
                    
        // Create the upload directory structure
        String path = UPLOADS_DIR + File.separator
                + calendar.get(Calendar.YEAR) + File.separator
                + (calendar.get(Calendar.MONTH) + 1) + File.separator;

        File uploadsPath = new File(getContextDir() + path);
        if (!uploadsPath.exists()) {
            uploadsPath.mkdirs();
        }
        
        // Get the preferred extension for the uploaded mime type
        String extension = findExtension(file.getContentType());

        // Create a web safe name for this file, you know, no spaces, no
        // strange characters, max length, and so on.
        String[] nameParts = StringUtil.splitFileNameExtension(file.getSubmittedFileName());
        nameParts[0] = StringUtil.toSlug(nameParts[0]);
        nameParts[1] = extension != null ? extension : StringUtil.toSlug(nameParts[1]);
        if (nameParts[0].length() + nameParts[1].length() > MAX_UPLOAD_FILE_NAME_LENGTH) {
            // minus 1 to remove the extra character between name and extension: "."
            nameParts[0] = nameParts[0].substring(0, 
                    MAX_UPLOAD_FILE_NAME_LENGTH - nameParts[1].length() - 1);
        }

        // File name may be empty if the value of submittedFileName is empty
        if (nameParts[0].length() > 0) {
            
            tempName = nameParts[0] + "." + nameParts[1];
            
            // Now, we have to check there's no other file with this name in
            // the target directory
            boolean exists = true;
            File f = new File(getContextDir() + path + tempName);
            
            // TODO: if there are more than 10000 files with the same
            // name, then we will overwrite the file whose name suffix equals "-10000"
            for (int i = 1; i < 10000 && exists; i++) {
                exists = f.exists();
                if (exists) {
                    // And try a different name if there's already a
                    // file with that name
                    tempName = nameParts[0]
                            + "-" + i + "."
                            + nameParts[1];
                    f = new File(getContextDir() + path + tempName);
                }
            }
        }
        // if tempName is empty, then assign a time stamp as name
        else {
            tempName = new Date().getTime() + "." + nameParts[1];
        }
        
        return path + tempName;
    }
    
    */
    /** 
     * Saves an uploaded file into the directory selected.
     * @param path the path and file name where the file is going to be saved.
     * @param part the HTTP part that contains the uploaded file information
     *             and data.
     * @throws IOException if there is an error while saving the data to disk.
     *//*
    private void saveUploadedFile(String path, Part part) throws IOException {
        OutputStream out = null;
        InputStream filecontent = null;
        
        try {
            out = new FileOutputStream(new File(getContextDir() + path));
            filecontent = part.getInputStream();
            int read;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        }
        finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
    }
    */
    
    /**
     * Returns a file extension for a given content type. It looks for the 
     * extension in the list of allowed mime types.
     * @param mimeType
     * @return a file extension or null if no file extension can be found.
     *//*
    private String findExtension(String mimeType) {
        
        return mimeTypes.get(mimeType);
        
    }*/
    
    
    /**
     * Looks for a given content type in the list of allowed mime types and
     * returns true if it's found or false if it's not.
     * 
     * @param mimeType
     * @return true if the mime type is found or false if not.
     *//*
    private boolean isAllowedContentType(String mimeType) {
        
        return mimeTypes.containsKey(mimeType);
            
    }*/
    

}
