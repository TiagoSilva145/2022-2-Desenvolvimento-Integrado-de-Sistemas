/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.model;

import cm.guititi.algoritmos.CGNE;
import cm.guititi.algoritmos.CGNR;
import cm.guititi.algoritmos.Util;
import java.time.LocalDateTime;
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
        result.startTime = LocalDateTime.now();
        
        String caminho = "matrizModelo/H-"+data.model+".csv";
        FloatMatrix model = CsvParser.readFloatMatrixFromCsvFile(caminho, ',');
        var g = CsvParser.readFloatMatrixFromCsvFile("dados-teste/G-"+ data.g + ".csv", ',');

        AlgorithmResult algResult;
        if(data.algorithm == AlgorithmEnum.CGNE)
            algResult = CGNE.run(model, data.imageSize, data.sampleLenght, data.sensorNum, g);
        else
            algResult = CGNR.run(model, data.imageSize, data.sampleLenght, data.sensorNum, g);
        
        result.IterNum = algResult.iterNum;
        
        FloatMatrix norm = Util.normaliza(algResult.image, 30, 30);
        result.imagePath = data.userId + "-" + result.imageId;
        Util.exporta_imagem(norm, result.imagePath, 30, 30);
        
        result.finishTime = LocalDateTime.now();
        
        return result;
    } 
}
