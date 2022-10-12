/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.server;

/**
 *
 * @author guilherme
 */
import com.guititi.model.Monitoramento;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

@RestController
public class MonitorController {
    
    @GetMapping("/monitor")
    public Monitoramento getMonitor() {
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
        monitor.FreePhisicalMemory = osBean.getFreePhysicalMemorySize();
        monitor.CommitedMemory = osBean.getCommittedVirtualMemorySize();
        monitor.CpuTime = osBean.getProcessCpuTime();
        monitor.ProcessCpuLoad = osBean.getProcessCpuLoad();
        return monitor;
    }
}
