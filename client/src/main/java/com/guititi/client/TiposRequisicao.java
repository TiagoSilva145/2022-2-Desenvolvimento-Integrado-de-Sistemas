/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.client;

import com.guititi.model.ImageResult;
import java.util.Random;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class TiposRequisicao {
    private Supplier<ImageResult> func;
    private Random random = new Random();
    
    public TiposRequisicao(Supplier<ImageResult> lambda) {
        func = lambda;
    }
    public void Rajada(int vezes, int tamanho, int milisecEntreRajadas) {
        for(int i=0; i < vezes; i++) {
            for(int j=0; j < tamanho; j++) {
                new Thread(() -> func.get()).start();
            }
            try {
                Thread.sleep(milisecEntreRajadas);
            } catch (InterruptedException ex) {
                Logger.getLogger(TiposRequisicao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Padrao(int vezes, int milisecEntre) {
        for(int i=0; i < vezes; i++) {
            new Thread(() -> func.get()).start();
            try {
                Thread.sleep(milisecEntre);
            } catch (InterruptedException ex) {
                Logger.getLogger(TiposRequisicao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Aleatorio(int vezes, int milisecPausaMax) {
        for(int i=0; i < vezes; i++) {
            new Thread(() -> func.get()).start();
            try {
                Thread.sleep(Math.abs(random.nextInt()) % milisecPausaMax);
            } catch (InterruptedException ex) {
                Logger.getLogger(TiposRequisicao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Unico() {
        Padrao(1, 0);
    }
}
