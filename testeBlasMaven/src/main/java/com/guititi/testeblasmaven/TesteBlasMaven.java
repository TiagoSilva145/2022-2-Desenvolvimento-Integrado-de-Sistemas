/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.guititi.testeblasmaven;

import com.guititi.testeblasmaven.CsvParser;
import org.jblas.FloatMatrix;
import java.io.*;
import java.util.*;

/**
 *
 * @author tiago145
 */
public class TesteBlasMaven {

    /**
     * Le o csv e coloca os valores em uma FloatMatrix
     * @param caminho_arquivo caminho do nome do arquivo
     * @return Uma FloatmMtrix correspondente aos conteudos do csv
     */
    static FloatMatrix le_csv(String caminho_arquivo)
    {
        FloatMatrix matrix = null;
        try {
            Scanner sc = new Scanner(new File(caminho_arquivo));
            sc.useDelimiter(";");
            
            List<Float> list_Floats = new ArrayList<Float>();
            while(sc.hasNext())
            {
                String aux = sc.next();
                try {
                    list_Floats.add(Float.parseFloat(aux));
                    
                    if(aux.endsWith("\n"))
                    {
                        if(matrix != null)
                            matrix = FloatMatrix.concatHorizontally(matrix, new FloatMatrix(list_Floats));
                        else
                            matrix = new FloatMatrix(list_Floats);
                            
                        list_Floats.clear();
                    }
                    
                } catch (Exception e) {
                    String[] dois_floats = aux.split("\n");
                    list_Floats.add(Float.parseFloat(dois_floats[0]));
                    
                    if(matrix != null)
                    {
                        matrix = FloatMatrix.concatHorizontally(matrix, new FloatMatrix(list_Floats));
                    }
                    else
                    {
                        matrix = new FloatMatrix(list_Floats);
                    }
                    
                    list_Floats.clear();
                    list_Floats.add(Float.parseFloat(dois_floats[1]));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return matrix.transpose();
    }
    
    static float calc_alfa(FloatMatrix r, FloatMatrix p)
    {
        FloatMatrix aux = r.transpose();
        aux = aux.mmul(r);
        
        FloatMatrix aux2 = p.transpose();
        aux2 = aux2.mmul(p);
        
        return aux.get(0,0) / aux2.get(0,0);
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
        FloatMatrix aux = r1.transpose();
        aux = aux.mmul(r1);
        
        FloatMatrix aux2 = r.transpose();
        aux2 = aux2.mmul(r);
        
        return aux.get(0,0) / aux2.get(0,0);
    }
    
    static FloatMatrix calc_p1(FloatMatrix modelo, FloatMatrix r1, FloatMatrix p, float beta)
    {
        FloatMatrix aux = modelo.transpose();
        aux = aux.mmul(r1);
        
        FloatMatrix aux2 = p.mul(beta);
        
        return aux.add(aux2);
    }
    
    static void cgne(String caminho_arquivo, int linhas, int colunas, int lado, int s, int n)
    {
        FloatMatrix modelo = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"H-1.csv", ',');
        FloatMatrix modeloTrans = modelo.transpose();
        
        float c = (modelo.mmul(modeloTrans)).norm1();
        
        //pra que serve o c?
        //g é puro ou precisa de ganho de sinal?
        
        FloatMatrix f = new FloatMatrix().zeros(linhas, colunas);
        FloatMatrix r = le_csv(caminho_arquivo+"G-1.csv");
        FloatMatrix p = modeloTrans.mmul(r);
        
       float erro = 1;
       
       while(erro > 0.0001)
       {
           //float alfa = ((r0.transpose()).mmul(r0)).get(0,0) / ((p0.transpose()).mmul(p1)).get(0,0);
           float alfa = calc_alfa(r, p);
           
           //FloatMatrix f1 = f0.add(p0.mul(alfa));
           FloatMatrix f1 = calc_f1(f, p, alfa);
           
           //FloatMatrix r1 = r0.sub((modelo.mmul(p0)).mul(alfa));
           FloatMatrix r1 = calc_r1(r, modelo, p, alfa);
           
           float beta = calc_beta(r1, r);
           
           FloatMatrix p1 = calc_p1(modelo, r1, p, beta);
           
           erro = r1.norm2() - r.norm2();
       }
       
        System.out.println("\n\n\nAcabou!!!");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        FloatMatrix m1 = new FloatMatrix(new float[][] {{0,1}, {2,3}});
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
        FloatMatrix a = CsvParser.readFloatMatrixFromCsvFile(caminho+"a.csv", ';');
        
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
        }
        
        cgne("matrizModelo/modelo1/", 50816, 3600, 60, 794, 64);
    }
    
}
