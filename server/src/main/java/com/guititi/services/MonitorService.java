/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.Monitoramento;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

/**
 *
 * @author guilherme
 */
@Service
public class MonitorService {
    public AtomicInteger ThreadsRunning = new AtomicInteger(0);
    private final long maxMemory = 4294967296L;
    public Monitoramento getStats() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);

        long allocatedMemory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
        long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;
        
        Monitoramento monitor = new Monitoramento();
        //monitor.CpuUsagePercent = osBean.getCpuLoad();
        monitor.MemoryUsagePercent = (double)allocatedMemory / (double)maxMemory;
        monitor.SwapUsagePercent = 1.0 - (double)osBean.getFreeSwapSpaceSize() / (double)osBean.getTotalSwapSpaceSize();
        monitor.FreeMemory = presumableFreeMemory;
        monitor.ProcessCpuLoad = osBean.getProcessCpuLoad();
        monitor.ThreadsRunning = ThreadsRunning.get();
        
        return monitor;
    }
    
    public synchronized boolean CanRun() {
        
        Monitoramento m = getStats();
        
        if(m.ThreadsRunning == 0)
            return true;
        

        if(m.MemoryUsagePercent > 0.8)
            return false;
        
        return true;
    }
}
