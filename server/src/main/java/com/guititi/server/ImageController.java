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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        request.algorithm = AlgorithmEnum.CGNE;
        request.algorithm.valor = request.alg;
        ResultWait wait = service.Enqueue(request);
        
        var result = wait.waitResult();
        
        return result;
    }
}
