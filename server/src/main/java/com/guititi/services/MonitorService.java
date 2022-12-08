/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.services;

import com.guititi.model.ConsumoMemoCPU;
import com.guititi.model.ImageRequest;
import com.guititi.model.Monitoramento;
import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    private boolean executando=false;
    private Thread monitorando; 
    private final long milisecMonitorando=4000L;
    private ArrayList<ConsumoMemoCPU> listaConsumo;
    
    public AtomicInteger ThreadsRunning = new AtomicInteger(0);
    
    private final OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final long maxMemory = 4294967296L;
    
    public Monitoramento getStats() {
        double usedMemory = (double)memoryMXBean.getNonHeapMemoryUsage().getUsed() +
                (double)memoryMXBean.getHeapMemoryUsage().getUsed();      
        double maxHeapMemory = (double)memoryMXBean.getNonHeapMemoryUsage().getMax() + 
                (double)memoryMXBean.getHeapMemoryUsage().getMax();
        
        Monitoramento monitor = new Monitoramento();
        monitor.CpuUsagePercent = osBean.getCpuLoad();
        monitor.MemoryUsagePercent = usedMemory / maxHeapMemory;
        monitor.SwapUsagePercent = 1.0 - (double)osBean.getFreeSwapSpaceSize() / (double)osBean.getTotalSwapSpaceSize();
        monitor.ThreadsRunning = ThreadsRunning.get();
        
        return monitor;
    }
    
    public synchronized boolean CanRun(ImageRequest request) {
        
        if(request == null)
            return false;
        
        Monitoramento m = getStats();
        
        if(!executando) {
            listaConsumo = new ArrayList<>();
            monitorando = new Thread(() -> this.monitorar());
            monitorando.start();
            executando = true;
        }
        
        if(m.ThreadsRunning == 0) {
            memoriaUtilizadaEstimada += EstimaQuantidadeMemoria(request.model);
            return true;
        }
        if(EstimaQuantidadeMemoria(request.model) > maxMemory) {
            return false;
        }
        
        memoriaUtilizadaEstimada += EstimaQuantidadeMemoria(request.model);
        return true;
    }
    
    public synchronized void DecrementThreadsRunning(int modelo) {
        memoriaUtilizadaEstimada -= EstimaQuantidadeMemoria(modelo);
        ThreadsRunning.getAndDecrement();
        
        if(ThreadsRunning.get() == 0) {
            GerarRelatorio(listaConsumo);
            monitorando.interrupt();
            executando = false;
        }
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
    
    private void monitorar() {
        boolean interromper = false;
        while(interromper == false) {
            Monitoramento m = this.getStats();
            listaConsumo.add(new ConsumoMemoCPU(m.CpuUsagePercent, m.MemoryUsagePercent));
            try {
                Thread.sleep(milisecMonitorando);
            } catch (InterruptedException ex) {
                interromper = true;
            }
        }
    }
    
    public void GerarRelatorio(ArrayList<ConsumoMemoCPU> lista) {
        System.out.println("Gerando grafico de desempenho do servidor");
        XYChart chart = new XYChart(1500, 1000);
        chart.setTitle("Relatório de consumo");
        chart.setXAxisTitle("Segundos");
        chart.setYAxisTitle("Porcentagem");

        double[] xData = new double[lista.size()];
        for(int i=0; i < lista.size(); i++) {
            xData[i] = (double)(milisecMonitorando * i) / 1000;
        }
        
        double[] yCPU = new double[lista.size()];
        double[] yMemoria = new double[lista.size()];
        
        for(int i=0; i < lista.size(); i++) {
            yCPU[i] = lista.get(i).CPU;
            yMemoria[i] = lista.get(i).Memoria;
        }

        chart.addSeries("CPU", xData, yCPU);
        chart.addSeries("Memoria", xData, yMemoria);
        
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            BitmapEncoder.saveBitmap(chart, "graficos/desempenho-"+formater.format(now), BitmapFormat.PNG);
        } catch (IOException ex) {
            Logger.getLogger(MonitorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
