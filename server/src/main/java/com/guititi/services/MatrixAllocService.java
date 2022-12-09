/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.CsvParser;
import java.util.HashMap;
import org.jblas.FloatMatrix;
import org.springframework.stereotype.Service;

/**
 *
 * @author guilherme
 */
@Service
public class MatrixAllocService {
    private HashMap<Integer, FloatMatrix> matrixMap = new HashMap<>();
    private HashMap<Integer, Integer> qtdThreads = new HashMap<>();
    public MatrixAllocService() {
        matrixMap.put(1, FloatMatrix.EMPTY);
        matrixMap.put(2, FloatMatrix.EMPTY);
        
        qtdThreads.put(1, 0);
        qtdThreads.put(2, 0);
    }
    
    public synchronized FloatMatrix getMatrix(Integer mNum) {
        if(matrixMap.get(mNum).equals(FloatMatrix.EMPTY)) {
            matrixMap.replace(mNum, allocMatrix(mNum));
        }
        
        qtdThreads.replace(mNum, qtdThreads.get(mNum)+1);
        return matrixMap.get(mNum);
    }
    
    private FloatMatrix allocMatrix(Integer mNum) {
        String caminho = "matrizModelo/H-"+mNum+".csv";
        return CsvParser.readFloatMatrixFromCsvFile(caminho, ',');
    }
    
    public synchronized void DecrementThread(Integer mNum) {
        qtdThreads.replace(mNum, qtdThreads.get(mNum)-1);
        
        if(qtdThreads.get(mNum) == 0) {
            matrixMap.replace(mNum, FloatMatrix.EMPTY);
            System.gc();
        }
    }
}
