/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.ImageRequest;
import com.guititi.model.QueueRequest;
import com.guititi.model.ResultWait;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author guilherme
 */
@Service
public class QueueService {
    private Queue<QueueRequest> queue = new LinkedList<>();
    Map<UUID, ResultWait> map = new HashMap<UUID, ResultWait>();
    
    public ResultWait Enqueue(ImageRequest data) {
        var request = new QueueRequest(data);
        queue.add(request);
        ResultWait wait = new ResultWait();
        map.put(request.requestId, wait);
        
        if(MonitorService.CanRun()) {
            Unqueue();
        } 
        
        return wait;
    }
    
    private void Unqueue() {
        var request = queue.poll();
        
        if(request == null)
            return;
        
        request.thread.start();
        try {
            request.thread.join();
        } catch (InterruptedException ex) {
        }
        
        map.get(request.requestId).result = request.result;
        map.get(request.requestId).semaphore.release();
        map.remove(request.requestId);
        
        //Run next thread
        Unqueue();
    }
}
