/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.model;

import cm.guititi.algoritmos.CGNE;
import cm.guititi.algoritmos.CGNR;
import cm.guititi.algoritmos.Util;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import org.jblas.FloatMatrix;

/**
 *
 * @author guilherme
 */
public class QueueRequest {

    public QueueRequest(ImageRequest dados) {
        requestId = UUID.randomUUID();
        this.data = dados;
        this.thread = new Thread(() -> this.threadFunc());
        this.thread.setPriority(2);
    }
    
    public UUID requestId;
    public Thread thread;
    public ImageRequest data;
    public ImageResult result;
    
    private ImageResult threadFunc() {  
        result = new ImageResult(data);
        result.imageId = requestId.toString();
        result.startTime = new Date();
        
        System.out.println("Executando requisição " + result.user + "-" + result.imageId);
        
        String caminho = "matrizModelo/H-"+data.model+".csv";
        FloatMatrix model = CsvParser.readFloatMatrixFromCsvFile(caminho, ',');

        AlgorithmResult algResult;
        if(data.algorithm == AlgorithmEnum.CGNE)
            algResult = CGNE.run(model, data.imageSize, data.sampleLenght, data.sensorNum, new FloatMatrix(data.g));
        else
            algResult = CGNR.run(model, data.imageSize, data.sampleLenght, data.sensorNum, new FloatMatrix(data.g));
        
        result.IterNum = algResult.iterNum;
        
        FloatMatrix norm = Util.normaliza(algResult.image, data.imageSize, data.imageSize);
        result.imagePath = data.userId + "-" + result.imageId + ".jpg";
        Util.exporta_imagem(norm, result.imagePath, data.imageSize, data.imageSize);
        
        result.finishTime = new Date();
        result.elapsedSeconds = (result.finishTime.getTime() - result.startTime.getTime()) / 1000;
        System.out.println("Finalizou requisição " + result.user + "-" + result.imageId);
        model = null;
        System.gc();
        
        return result;
    } 
}
