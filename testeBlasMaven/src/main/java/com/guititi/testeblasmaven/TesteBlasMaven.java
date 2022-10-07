/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.guititi.testeblasmaven;

import com.guititi.testeblasmaven.CsvParser;
import java.awt.Color;
import java.awt.image.BufferedImage;
import org.jblas.FloatMatrix;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.imageio.ImageIO;

/**
 *
 * @author tiago145
 */
public class TesteBlasMaven {

    /*
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
    */
    
    /**
     * Aplica o algoritmo cgne
     * @param caminho_arquivo caminho para os arquivos base
     * @param lado lado da imagem
     * @param s S para o ganho de sinal
     * @param n N para o ganho de sinal
     * @return 
     */
    static FloatMatrix cgne(String caminho_arquivo, int lado, int s, int n)
    {
        FloatMatrix modelo = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"H-1.csv", ',');
        
        FloatMatrix f = new FloatMatrix().zeros(1, lado * lado);
        FloatMatrix r = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"G-2.csv", ',');
        FloatMatrix p = (modelo.transpose()).mmul(r);
        
       float erro = 1;
       
       while(erro > 0.0001)
       //for(int i = 0; i < 100; i++)
       {
           float alfa = ((r.transpose()).dot(r))/((p.transpose()).dot(p));
           f = f.add(p.mmul(alfa));
           //FloatMatrix r1 = r.sub((modelo.mmul(p)).mul(alfa));
           FloatMatrix r1 = r.sub((modelo.mul(alfa)).mmul(p));
           
           erro = r1.norm2() - r.norm2();
           erro = Math.abs(erro);
           
           float beta = ((r1.transpose()).dot(r1))/((r.transpose()).dot(r));
           p = ((modelo.transpose()).mmul(r1)).add(p.mul(beta));
           r = r1;
           
           
           /*
           float alfa = calc_alfa(r, p);
           f = calc_f1(f, p, alfa);
           FloatMatrix r1 = calc_r1(r, modelo, p, alfa);
           float beta = calc_beta(r1, r);
           p = calc_p1(modelo, r1, p, beta);
           r = r1;
           */
           
       }
       
        return f;
    }
    
    static FloatMatrix cgnr(String caminho_arquivo, int lado, int s, int n)
    {
        FloatMatrix modelo = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"H-1.csv", ',');
        
        FloatMatrix f = new FloatMatrix().zeros(1, lado * lado);
        FloatMatrix r = CsvParser.readFloatMatrixFromCsvFile(caminho_arquivo+"G-2.csv", ',');
        FloatMatrix z = (modelo.transpose()).mmul(r);
        FloatMatrix p = z;
        
       float erro = 1;
       
       while(erro > 0.0001)
       {
           FloatMatrix w = modelo.mmul(p);
           
           //-------------------- pode dar errado float pra double
           float alfa = (float)(Math.pow(z.norm2(), 2)/Math.pow(w.norm2(), 2));
           //---------------------checar aqui caso erro
           f = f.add(p.mul(alfa));
           FloatMatrix r1 = r.sub(w.mul(alfa));
           
           erro = r1.norm2() - r.norm2();
           erro = Math.abs(erro);
           
           FloatMatrix z1 = (modelo.transpose()).mmul(r1);
           
           //-------------------- pode dar errado float pra double
           float beta = (float)(Math.pow(z1.norm2(), 2)/Math.pow(z.norm2(), 2));
           //---------------------checar aqui caso erro
           
           p = z1.add(p.mul(beta));
           
           z = z1;
           r = r1;
       }
       
       return f;
    }
    
    /**
     * Normaliza a matriz de float para uma matriz no intervalo [0,255]
     * @param matriz um vetor do tipo v[lin*col]
     * @param lin quantidade de linhas da matriz
     * @param col quantidade de colunas da matriz
     * @return uma matriz m[lin][col] normalizada
     */
    static FloatMatrix normaliza(FloatMatrix matriz, int lin, int col)
    {
        float min = matriz.min();
        float max = matriz.max();
        float dist = max - min;
        
        FloatMatrix m = new FloatMatrix().zeros(lin, col);
        
        for(int i = 0; i < lin; i++)
            for(int j = 0; j < col; j++)
                m.put(i, j, ((matriz.get(i*lin + j)) - min) / dist * 255);
        
        return m;
    }
    
    /**
     * Exporta a imagem em greyscale
     * @param matriz a matriz da imagem (com valores [0,255])
     * @param nome nome do arquivo da imagem que sera criado
     * @param lin quantidade de linhas da matriz
     * @param col quantidade de colunas da matria
     */
    static void exporta_imagem(FloatMatrix matriz, String nome, int lin, int col)
    {
        BufferedImage imagem = new BufferedImage(lin, col, BufferedImage.TYPE_BYTE_GRAY);
        
        for(int i = 0; i < lin; i++)
            for(int j = 0; j < col; j++)
            {
                int elem = (int) matriz.get(i,j);
                Color cor = new Color(elem,elem,elem);
                imagem.setRGB(i, j, cor.getRGB());
            }
        
        File arq = new File("./"+nome);
        try {
            ImageIO.write(imagem, "jpg", arq);
        } catch (Exception e) {
            System.out.println("Erro na gravacao da imagem: " + e);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String caminho_arquivo = "matrizModelo/modelo2/";
        //FloatMatrix imagem = cgne(caminho_arquivo, 30, 436, 64);
        FloatMatrix imagem = cgnr(caminho_arquivo, 30, 436, 64);
        FloatMatrix norm = normaliza(imagem, 30, 30);
        exporta_imagem(norm, "teste", 30, 30);
        
    }
    
}
