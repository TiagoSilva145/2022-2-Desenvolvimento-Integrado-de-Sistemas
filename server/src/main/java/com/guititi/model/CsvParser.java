/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.model;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import java.io.FileReader;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.jblas.FloatMatrix;

public class CsvParser {
    public static FloatMatrix readFloatMatrixFromCsvFile(String matrixName, char separator) {
        long start = System.currentTimeMillis();

        System.out.println("Carregando modelo " + matrixName);
        FloatMatrix matrix = null;
        List<float[]> matrixLines = new LinkedList<>();
        
        try {
            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(new File(matrixName)))
                                  .withCSVParser(parser)
                                  .build();
            
            //Utiliza matriz pois o jblas só constroi FloatMatrix com uma matriz (1,n) 
            //a partir de uma matriz
            String[] values;
            int j = 0;
            while ((values = csvReader.readNext()) != null) {
                //Só sabemos quantas colunas os arquivo têm depois de lê-lo, por isso declarar aqui
                float[] matrixLine = new float[values.length];
                
                int i = 0;
                for(String value : values) {
                    matrixLine[i++] = Double.valueOf(value).floatValue();
                }
                matrixLines.add(matrixLine);
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        
        int i=0;
        float[][] grouped = new float[matrixLines.size()][matrixLines.get(0).length];
        for(float[] line : matrixLines) {
            grouped[i++] = line;
        }
        
        matrix = new FloatMatrix(grouped);
        
        long finish = System.currentTimeMillis();
        long timeElapsed = (finish - start);
        System.out.println("Carregado modelo em " + Long.toString(timeElapsed) + "milissegundos " + matrixName);
        return matrix;
    }
    
    public static FloatMatrix readModelMatrixFromCsvFile(String matrixName, char separator, int rows, int columns) {
        long start = System.currentTimeMillis();
        System.out.println("Carregando modelo " + matrixName);
        FloatMatrix matrix = null;
        float[][] tempMatrix = new float[rows][columns];
        try {
            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            CSVReader csvReader = new CSVReaderBuilder(new FileReader(new File(matrixName)))
                                  .withCSVParser(parser)
                                  .build();
            
            //Utiliza matriz pois o jblas só constroi FloatMatrix com uma matriz (1,n) 
            //a partir de uma matriz
            String[] values;
            int i=0, j=0;
            while ((values = csvReader.readNext()) != null) {
                j=0;
                for(String value : values) {
                    tempMatrix[i][j++] = Float.valueOf(value);
                }
                i++;
            }
        } catch(Exception e) {
            System.err.println(e);
        }
        
        matrix = new FloatMatrix(tempMatrix);
        long finish = System.currentTimeMillis();
        long timeElapsed = (finish - start);
        System.out.println("Carregado modelo em " + Long.toString(timeElapsed) + "milissegundos " + matrixName);
        return matrix;
    }
}
