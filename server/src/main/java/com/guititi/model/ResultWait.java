/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.model;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guilherme
 */
public class ResultWait {
    public Semaphore semaphore = new Semaphore(0);
    public ImageResult result;
    
    public ImageResult waitResult() {
        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(ResultWait.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
