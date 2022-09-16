/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package testeblas;

import org.jblas.FloatMatrix;
import java.io.*;
import java.util.*;

/**
 *
 * @author tiago145
 */
public class TesteBlas {

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
        FloatMatrix a = le_csv(caminho+"a.csv");
        a.print();
        
        System.out.print("Matriz M: ");
        FloatMatrix m = le_csv(caminho+"M.csv");
        m.print();
        
        System.out.print("Matriz N: ");
        FloatMatrix n = le_csv(caminho+"N.csv");
        n.print();
        
        System.out.println("");
        
        System.out.println("Operacoes: ");
        
        System.out.println("");
        System.out.println("MN: ");
        System.out.println("Espera-se " + le_csv(caminho+"MN.csv"));
        System.out.println("Temos:    " + m.mmul(n));
        
        System.out.println("");
        System.out.println("aM: ");
        System.out.println("Espera-se " + le_csv(caminho+"aM.csv"));
        System.out.println("Temos:    " + a.mmul(m));
        
    }
    
}
