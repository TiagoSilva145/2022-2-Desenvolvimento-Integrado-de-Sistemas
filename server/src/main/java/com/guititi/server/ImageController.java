/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.server;

import com.guititi.model.AlgorithmEnum;
import com.guititi.model.ImageRequest;
import com.guititi.model.ImageResult;
import com.guititi.model.ResultWait;
import com.guititi.services.QueueService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author guilherme
 */
@RestController
public class ImageController {
    @Autowired
    QueueService service;
    
    @PostMapping("/image")
    public ImageResult post(@RequestBody ImageRequest request) {
        if(request.alg == 1)
            request.algorithm = AlgorithmEnum.CGNE;
        else
            request.algorithm = AlgorithmEnum.CGNR;
        
        ImageResult result = service.Enqueue(request);
        
        return result;
    }
    
    @RequestMapping(value = "/image/{file_name}", method = RequestMethod.GET)
    public void getFile(
        @PathVariable("file_name") String fileName, 
        HttpServletResponse response) {
        response.setContentType("application/jpg");
        try {
          // get your file as InputStream
          InputStream is = new FileInputStream(new File("images/"+fileName));
          // copy it to response's OutputStream
          IOUtils.copy(is, response.getOutputStream());
          response.flushBuffer();
        } catch (IOException ex) {
          System.out.println("Error writing file to output stream. Filename was "+ fileName + ex);
        }

    }
}
