/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cm.guititi.algoritmos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.jblas.FloatMatrix;

/**
 *
 * @author guilherme
 */
public class Util {
    public static FloatMatrix normaliza(FloatMatrix matriz, int lin, int col)
    {
        float min = matriz.min();
        float max = matriz.max();
        float dist = max - min;
        
        FloatMatrix m = new FloatMatrix().zeros(lin, col);
        
        for(int i = 0; i < lin; i++)
            for(int j = 0; j < col; j++)
                m.put(i, j, ((matriz.get(i*lin + j)) - min) / dist * 255);
        
        return m;
    }
    
    public static void exporta_imagem(FloatMatrix matriz, String nome, int lin, int col)
    {
        BufferedImage imagem = new BufferedImage(lin, col, BufferedImage.TYPE_BYTE_GRAY);
        
        for(int i = 0; i < lin; i++)
            for(int j = 0; j < col; j++)
            {
                int elem = (int) matriz.get(i,j);
                Color cor = new Color(elem,elem,elem);
                imagem.setRGB(i, j, cor.getRGB());
            }
        
        File arq = new File("images/"+nome);
        try {
            ImageIO.write(imagem, "jpg", arq);
        } catch (Exception e) {
            System.out.println("Erro na gravacao da imagem: " + e);
        }
    }
}
