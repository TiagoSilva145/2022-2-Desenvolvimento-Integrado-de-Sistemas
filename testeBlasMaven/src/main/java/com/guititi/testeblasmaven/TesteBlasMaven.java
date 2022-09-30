/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.guititi.testeblasmaven;

import com.guititi.testeblasmaven.CsvParser;
import java.awt.image.BufferedImage;
import org.jblas.FloatMatrix;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author tiago145
 */
public class TesteBlasMaven {

    
    static float calc_alfa(FloatMatrix r, FloatMatrix p)
    {
        FloatMatrix rtrans = r.transpose();
        FloatMatrix ptrans = p.transpose();
        
        float num = rtrans.dot(r);
        float den = ptrans.dot(p);
        
        return num/den;
    }
    
    static FloatMatrix calc_f1(FloatMatrix f, FloatMatrix p, float alfa)
    {
        FloatMatrix aux = p.mul(alfa);
        return f.add(aux);
    }
    
    static FloatMatrix calc_r1(FloatMatrix r, FloatMatrix modelo, FloatMatrix p, float alfa)
    {
        FloatMatrix aux = modelo.mmul(p);
        aux = aux.mul(alfa);
        return r.sub(aux);
    }
    
    static float calc_beta(FloatMatrix r1, FloatMatrix r)
    {
        FloatMatrix rtrans = r.transpose();
        FloatMatrix r1trans = r1.transpose();
        
        float num = r1trans.dot(r1);
        float den = rtrans.dot(r);
        
        return num/den;
    }
    
    static FloatMatrix calc_p1(FloatMatrix modelo, FloatMatrix r1, FloatMatrix p, float beta)
    {
        FloatMatrix aux = modelo.transpose();
        aux = aux.mmul(r1);
        
        FloatMatrix aux2 = p.mul(beta);
        
        return aux.add(aux2);
    }
    
    static FloatMatrix cgne(String caminho_arquivo, int linhas, int colunas, int lado, int s, int n)
    {
        FloatMatrix modelo = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"H-1.csv", ',');
        FloatMatrix modeloTrans = modelo.transpose();
        
        /*System.out.println("Modelo: linhas e colunas: " + modelo.rows + " " + modelo.columns);
        System.out.println("Modelo trans: linhas e colunas: " + modeloTrans.rows + " " + modeloTrans.columns);
        
        try {  
            modeloTrans.mmul(modelo);
        } catch (Exception e) {
            System.out.println("-------- ERRO --------: "+e);
        }*/
        
        float c = (modeloTrans.mmul(modelo)).norm1();
        
        //pra que serve o c?
        //g é puro ou precisa de ganho de sinal?
        
        FloatMatrix f = new FloatMatrix().zeros(lado * lado, 1);
        FloatMatrix r = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"G-1.csv", ',');
        FloatMatrix p = modeloTrans.mmul(r);
        
       float erro = 1;
       
       //while(erro > 0.0000001)
       for(int i = 0; i < 1; i++)
       {
           //System.out.println("a");
           //float alfa = ((r0.transpose()).mmul(r0)).get(0,0) / ((p0.transpose()).mmul(p1)).get(0,0);
           float alfa = calc_alfa(r, p);
           
           //FloatMatrix f1 = f0.add(p0.mul(alfa));
           f = calc_f1(f, p, alfa);
           
           //FloatMatrix r1 = r0.sub((modelo.mmul(p0)).mul(alfa));
           FloatMatrix r1 = calc_r1(r, modelo, p, alfa);
           
           erro = r1.norm2() - r.norm2();
           //System.out.println("Norm r1: " + r1.norm2());
           //System.out.println("Norm r: " + r.norm2());
           erro = Math.abs(erro);
           System.out.println("Erro: " + erro);
           System.out.println("");
           
           float beta = calc_beta(r1, r);
           
           p = calc_p1(modelo, r1, p, beta);
           r = r1;
       }
       
        System.out.println("Acabou!!!");
        f.print();
        return f;
    }
    
    /*static void exporta_imagem(FloatMatrix matrix, String nome)
    {
        BufferedImage imagem;
        
        for(int i = 0; i < matrix.rows; i++)
        {
            for(int j = 0; j < matrix.columns; j++)
            {
                float a = matrix.get(i,j);
                Color cor = newColor(a,a,a);
                
            }
        }
    }*/
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*FloatMatrix m1 = new FloatMatrix(new float[][] {{0,1}, {2,3}});
        FloatMatrix m2 = new FloatMatrix(new float[][] {{1,2}, {3,4}});
        FloatMatrix m3 = new FloatMatrix(new float[][] {{1},{2}});
        FloatMatrix m4 = new FloatMatrix(new float[][] {{1,2}});
        
        System.out.println("Matrizes:");
        m1.print();
        m2.print();
        m3.print();
        m4.print();
        System.out.println("");
        
        
        System.out.println("Multiplicando m1 * m2:");
        System.out.println("Espera-se: [3,4,11,16]");
        System.out.println(m1.mmul(m2));
        System.out.println("");
        
        System.out.println("Multiplicando m1 * m3");
        System.out.println("Espera-se: [2,8]");
        System.out.println(m1.mmul(m3));
        System.out.println("");
        
        System.out.println("Multiplicando m4 * m1");
        System.out.println("Espera-se: [4,7]");
        System.out.println(m4.mmul(m1));
        System.out.println("");
        
        ////////----------------------------------------------//////////
        System.out.println("Testando com dados do moodle:");
        
        String caminho = "dados-teste/";
        
        System.out.print("Vetor  a: ");
        FloatMatrix a = CsvParser.readFloatMatrixFromCsvFile(caminho+"a.csv", ',');
        
        DecimalFormat df = new DecimalFormat();
        df. setMaximumFractionDigits(20);
        System.out.println("");
        System.out.println(df. format(a.get(0,0)));
        
        a.print();
        
        System.out.print("Matriz M: ");
        FloatMatrix m = CsvParser.readFloatMatrixFromCsvFile(caminho+"M.csv", ';');
        m.print();
        
        System.out.print("Matriz N: ");
        FloatMatrix n = CsvParser.readFloatMatrixFromCsvFile(caminho+"N.csv", ';');
        n.print();
        
        System.out.println("");
        
        System.out.println("Operacoes: ");
        
        System.out.println("");
        System.out.println("MN: ");
        System.out.println("Espera-se " + CsvParser.readFloatMatrixFromCsvFile(caminho+"MN.csv", ';'));
        System.out.println("Temos:    " + m.mmul(n));
        
        System.out.println("");
        System.out.println("aM: ");
        System.out.println("Espera-se " + CsvParser.readFloatMatrixFromCsvFile(caminho+"aM.csv", ';'));
        System.out.println("Temos:    " + a.mmul(m));
        
        try {
            System.out.println("");
            System.out.println("Ma: ");
            System.out.println("Espera-se erro");
            System.out.println("Temos:    " + m.mmul(a));
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }*/
        
        /*
        String caminho_arquivo = "matrizModelo/modelo1/";
        cgne(caminho_arquivo, 50816, 3600, 60, 794, 64);
        */
        
        String caminho_arquivo = "matrizModelo/modelo2/";
        FloatMatrix imagem = cgne(caminho_arquivo, 27904, 900, 30, 436, 64);
    }
    
}
