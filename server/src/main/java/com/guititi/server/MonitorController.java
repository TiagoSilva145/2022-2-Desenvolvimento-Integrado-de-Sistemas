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
import com.guititi.services.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {
    @Autowired
    private MonitorService monitor;
    
    @GetMapping("/monitor")
    public Monitoramento getMonitor() {
        return monitor.getStats();
    }
}
