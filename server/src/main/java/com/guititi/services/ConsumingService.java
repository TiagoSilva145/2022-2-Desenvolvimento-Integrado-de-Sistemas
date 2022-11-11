/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.CsvParser;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.jblas.FloatMatrix;
import org.springframework.stereotype.Service;

/**
 *
 * @author guilherme
 */
@Service
public class ConsumingService {
    private Random random = new Random();
    private List<FloatMatrix> matrizes = new LinkedList<FloatMatrix>();
    private String caminho_arquivo = "matrizModelo/H-1.csv";
    private int alternar = 1;
    
    public void ExecuteConsumingProcess(int num, int size) {
        float [][] matrix = new float[size][size];
        for(int i=0; i < size; i++) {
            for(int j=0; j < size; j++) {
                matrix[i][j] = random.nextFloat();
            }
        }
        
        FloatMatrix fm = new FloatMatrix(matrix);
        FloatMatrix aux;
        for(int i=0; i < num; i++) {
            fm = fm.mmul(fm);
        }
    }
    
    public void matrixAllocation() {
        FloatMatrix modelo;
        if(alternar++ % 2 == 0) {
            System.out.println("velho");
            modelo = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo, ',');
        }
        else {
            System.out.println("novo");
            modelo = CsvParser.readModelMatrixFromCsvFile(caminho_arquivo, ',', 50816, 3600);
        }
        matrizes.add(modelo);
    }
    public void matrixDeallocation() {
        if(matrizes.size() > 0)
            matrizes.remove(0);
        System.gc();
    }
}
