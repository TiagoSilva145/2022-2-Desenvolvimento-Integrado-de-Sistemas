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
    final long tamanhoH1 = 731750400L * 2L;
    final long tamanhoH2 = 100454400L * 2L;
    
    long memoriaUtilizadaEstimada = 0;
    
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
    
    public synchronized boolean CanRun(int modelo) {
        
        Monitoramento m = getStats();
        
        if(m.ThreadsRunning == 0) {
            memoriaUtilizadaEstimada += EstimaQuantidadeMemoria(modelo);
            return true;
        }
        if(EstimaQuantidadeMemoria(modelo) > maxMemory) {
            return false;
        }
        
        //if(m.MemoryUsagePercent > 0.8)
        /*    return false;*/
        memoriaUtilizadaEstimada += EstimaQuantidadeMemoria(modelo);
        return true;
    }
    
    public synchronized void DecrementThreadsRunning(int modelo) {
        memoriaUtilizadaEstimada -= EstimaQuantidadeMemoria(modelo);
        ThreadsRunning.getAndDecrement();
    }
    
    private long EstimaQuantidadeMemoria(int modelo) {
        long memoriaEstimada = memoriaUtilizadaEstimada;
        
        if(modelo == 1) {
            memoriaEstimada += tamanhoH1;
        }
        else {
            memoriaEstimada += tamanhoH2;
        }
        
        return memoriaEstimada;
    }
}
