/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.Monitoramento;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author guilherme
 */
public class MonitorService {
    public static Monitoramento getStats() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        
        Monitoramento monitor = new Monitoramento();
        monitor.CpuUsagePercent = osBean.getCpuLoad();
        monitor.MemoryUsagePercent = 1.0 - (double)osBean.getFreeMemorySize() / (double)osBean.getTotalMemorySize();
        monitor.SwapUsagePercent = 1.0 - (double)osBean.getFreeSwapSpaceSize() / (double)osBean.getTotalSwapSpaceSize();
        monitor.FreeMemory = osBean.getFreeMemorySize();
        monitor.TotalMemory = osBean.getTotalMemorySize();
        monitor.FreeSwap = osBean.getFreeSwapSpaceSize();
        monitor.TotalSwap = osBean.getTotalSwapSpaceSize();
        monitor.CommitedMemory = osBean.getCommittedVirtualMemorySize();
        monitor.ProcessCpuLoad = osBean.getProcessCpuLoad();
        
        return monitor;
    }
    public static boolean CanRun() {
        
        Monitoramento m = getStats();
        
        if(m.MemoryUsagePercent > 0.9)
            return false;
        
        return true;
    }
}
