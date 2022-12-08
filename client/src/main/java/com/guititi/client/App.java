/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.client;

import com.github.javafaker.Faker;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.guititi.model.ImageRequest;
import com.guititi.model.ImageResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.jblas.FloatMatrix;

/**
 *
 * @author guilherme
 */
public class App {
    Gson jsonSerializer = new Gson();
    Faker faker = new Faker();
    private String caminho_arquivo = "dados/modelo";
    public App () {
        
    }
    
    public ImageResult Requisita() {
        HttpClient httpClient = HttpClient.newHttpClient();
        String json = fazRequisicao();
        HttpRequest request = HttpRequest.newBuilder(
        URI.create("http://localhost:8080/image"))
            .POST(BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .build();

         // use the client to send the request
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println(response.body());

        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        String body = response.body();
        ImageResult result = jsonSerializer.fromJson(response.body(), ImageResult.class);
        
        BaixarImagem(result);
        new Relatorio(result).CriarRelatorio();
        return result;
    }
    
    public String fazRequisicao()
    {
        Random aleatorio = new Random();
        
        ImageRequest req = new ImageRequest();
        
        req.userId = faker.name().firstName();
        req.model = Math.abs((aleatorio.nextInt())%2)+1;
        req.alg = Math.abs((aleatorio.nextInt())%2);
        int imagem = Math.abs((aleatorio.nextInt())%3)+1;
        String g_path = caminho_arquivo+Integer.toString(req.model)+"/"+"G-"+imagem+".csv";
        
        if(Math.abs((aleatorio.nextInt())%2) == 0) {
            req.g = CsvParser
                    .readFloatMatrixFromCsvFile(g_path, ',')
                    .toArray2();
        }
        else {
            req.g = ganhoSinal(imagem, req.model, g_path);
        }
        
        req.g = ganhoSinal(imagem, req.model, g_path);
        
        if(req.model == 1)
        {
            req.imageSize = 60;
            req.sampleLenght = 794;
            req.sensorNum = 64;
        }
        else
        {
            req.imageSize = 30;
            req.sampleLenght = 436;
            req.sensorNum = 64;
        }
        
        System.out.println("requisitando nome: " + req.userId
                + " modelo: " + Integer.toString(req.model)
                + " g:" + Integer.toString(imagem)
                + " alg: " + Integer.toString(req.alg));
        
        return jsonSerializer.toJson(req);
    }
    public float[][] ganhoSinal(int g, int model, String g_path)
    {
        int n;
        int s;
        int tam;
        String imagem;
        
        if(model == 1) //modelo 1
        {
            n = 64;
            s = 794;
            tam = 60;
        }
        else //modelo 2
        {
            n = 64;
            s = 436;
            tam = 30;
        }
        
        if(g == 1)
            imagem = "1";
        else
            imagem = "2";
        
        FloatMatrix r = CsvParser.readFloatMatrixFromCsvFile(g_path, ',');
        
        for(int c = 0; c < n; c++)
        {
            for(int l = 0; l < s; l++)
            {
                int index = s * c + l;
                //pode dar errado aqui
                float sinal = 100 + 1/20 * l * (float) Math.sqrt(l);
                
                r.put(index, r.get(index) * sinal);
            }
        }
        return r.toArray2();
    }
    
    private void BaixarImagem(ImageResult result) {
        try {
            URL url = new URL("http://localhost:8080/image/" + result.imagePath);
            FileUtils.copyURLToFile(
                url, 
                new File("relatorios/imagens/"+result.imagePath));
        } catch (MalformedURLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
