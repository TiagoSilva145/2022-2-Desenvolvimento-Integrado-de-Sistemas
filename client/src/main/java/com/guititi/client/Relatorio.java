/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.guititi.client;

import com.guititi.model.ImageResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.StringSubstitutor;

/**
 *
 * @author guilherme
 */
public class Relatorio {
    private final File template = new File("templateRelatorio.html");
    private Map<String, String> valoresRealtorio = new HashMap<>();
    ImageResult dados;
    
    public Relatorio(ImageResult result) {
        dados = result;
        valoresRealtorio.put("nome-requisicao", result.imageId);
        valoresRealtorio.put("usuario", result.user);
        valoresRealtorio.put("algoritmo", result.alg);
        valoresRealtorio.put("iteracoes", Integer.toString(result.IterNum));
        valoresRealtorio.put("tamanho-imagem", Integer.toString(result.ImageSize));
        valoresRealtorio.put("data-inicio", result.startTime.toString());
        valoresRealtorio.put("tempo-proc", Long.toString(result.elapsedSeconds));
        valoresRealtorio.put("caminho-imagem", "imagens/" + result.imagePath);
    }
    
    public void CriarRelatorio() {
        String html="";
        
        try {
            StringBuilder builder = readFromInputStream(new FileInputStream(template));
            html = builder.toString();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ioex) {
                   
        }
        
        try {
            String templatePreenchido = PreencherTemplate(html);
            try (PrintWriter out = new PrintWriter("relatorios/"+dados.user+"-"+dados.imageId+".html")) {
                out.print(templatePreenchido);
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String PreencherTemplate(String template) {
        StringSubstitutor sub = new StringSubstitutor(valoresRealtorio);
        String result = sub.replace(template);
        return result;
    }
    
    private StringBuilder readFromInputStream(InputStream inputStream)
    throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
          = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder;
    }
}
