/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cm.guititi.algoritmos;

import com.guititi.model.AlgorithmResult;
import com.guititi.model.CsvParser;
import org.jblas.FloatMatrix;

/**
 *
 * @author guilherme
 */
public class CGNR {
    public static AlgorithmResult run(FloatMatrix model, int size, int s, int n, FloatMatrix g) {
        System.out.println("Executando CGNR");
        var result = new AlgorithmResult();

        FloatMatrix f = new FloatMatrix().zeros(1, size * size);
        FloatMatrix r = g;
        FloatMatrix z = (model.transpose()).mmul(r);
        FloatMatrix p = z;
        
       float erro = 1;
       
       while(erro > 0.0001)
       {
           FloatMatrix w = model.mmul(p);
           
           //-------------------- pode dar errado float pra double
           float alfa = (float)(Math.pow(z.norm2(), 2)/Math.pow(w.norm2(), 2));
           //---------------------checar aqui caso erro
           f = f.add(p.mul(alfa));
           FloatMatrix r1 = r.sub(w.mul(alfa));
           
           erro = r1.norm2() - r.norm2();
           erro = Math.abs(erro);
           
           FloatMatrix z1 = (model.transpose()).mmul(r1);
           
           //-------------------- pode dar errado float pra double
           float beta = (float)(Math.pow(z1.norm2(), 2)/Math.pow(z.norm2(), 2));
           //---------------------checar aqui caso erro
           
           p = z1.add(p.mul(beta));
           
           z = z1;
           r = r1;
           result.iterNum++;
       }
       System.out.println("Finalizado CGNR");
       
       result.image = f;
       return result;
    }
}
