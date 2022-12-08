/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.server;

/**
 *
 * @author guilherme
 */
import com.guititi.model.ConsumingRequest;
import com.guititi.model.ConsumoMemoCPU;
import com.guititi.services.ConsumingService;
import com.guititi.services.MonitorService;
import java.util.ArrayList;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConsumingController {
    @Autowired
    ConsumingService service;
    
    @Autowired
    MonitorService monitor;
    
    @GetMapping("/consume")
    public String index() {
        service.ExecuteConsumingProcess(100, 100);
        return "Greetings from Spring Boot!";
    }
    
    @PostMapping("/consume")
    public String post(@RequestBody ConsumingRequest request) {
        service.ExecuteConsumingProcess(request.num, request.size);
        return "it works!";
    }
    
    @PostMapping("/consume/allocate")
    public String allocate() {
        service.matrixAllocation();
        return "allocated";
    }
    @PostMapping("/consume/deallocate")
    public String deallocate() {
        service.matrixDeallocation();
        return "deallocated";
    }
    
    @PostMapping("/consume/relatorio")
    public void relatorio() {
        Random rd = new Random();
        ArrayList<ConsumoMemoCPU> con = new ArrayList<>();
        for(int i=0; i < 30; i++) {
            con.add(new ConsumoMemoCPU(rd.nextDouble(), rd.nextDouble()));
        }
        
        monitor.GerarRelatorio(con);
    }
}
