/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.ImageRequest;
import com.guititi.model.ImageResult;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author guilherme
 */
@Service
public class QueueService {
    @Autowired
    private MonitorService monitor;
    
    private Queue<QueueRequest> queue = new LinkedList<>();
    Map<UUID, ResultWait> map = new HashMap<UUID, ResultWait>();

    private final Object queueLock = new Object();
    private final Object mapLock = new Object();
    
    public ImageResult Enqueue(ImageRequest data) {
        QueueRequest request = new QueueRequest(data);
        QueueAdd(request);
        ResultWait wait = new ResultWait();
        MapPut(request.requestId, wait);
        if(monitor.CanRun()) {
            new Thread(() -> Unqueue()).start();
        }
        else {
            
            new Thread(() -> TryRun(2)).start();
        }
        
        return wait.waitResult();
    }
    
    private void Unqueue() {
        QueueRequest request = QueuePoll();
        if(request == null)
            return;
        
        monitor.ThreadsRunning.incrementAndGet();
        request.thread.start();
        try {
            request.thread.join();
        } catch (InterruptedException ex) {
        }

        monitor.ThreadsRunning.decrementAndGet();

        ResultWait waited = MapPoll(request.requestId);
        waited.result = request.result;
        waited.semaphore.release();
        
        //Run next thread
        if(monitor.CanRun()) {
            Unqueue();
        }
        else {
            new Thread(() -> TryRun(2)).start();
        }
    }

    private void TryRun(int seconds) {
        System.out.println("Não pode rodar agora, aguardando " + 
                Integer.toString(seconds) + "segundos para tentar novamente");
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        if(monitor.CanRun()) {
            Unqueue();
        }
        else {
            TryRun(seconds * 2);
        }
    }

    private void QueueAdd(QueueRequest request) {
        synchronized (queueLock) {
            queue.add(request);
        }
    }

    private QueueRequest QueuePoll() {
        QueueRequest result;
        synchronized (queueLock) {
            result = queue.poll();
        }
        return result;
    }

    private void MapPut(UUID id, ResultWait wait) {
        synchronized(mapLock) {
            map.put(id, wait);
        }
    }

    private ResultWait MapPoll(UUID id) {
        ResultWait waited;
        synchronized(mapLock) {
            waited = map.get(id);
            map.remove(id);
        }
        return waited;
    }
}
