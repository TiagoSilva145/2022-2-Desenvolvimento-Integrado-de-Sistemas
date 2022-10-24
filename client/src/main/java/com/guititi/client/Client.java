/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.guititi.client;

import java.util.Scanner;

/**
 *
 * @author guilherme
 */
public class Client {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        App app = new App();
        TiposRequisicao tipos = new TiposRequisicao(() -> app.Requisita());
        
        Scanner scan = new Scanner(System.in);
        int opcao = -1;
        do {
            ImprimeOpcoes();
            opcao = scan.nextInt();
            switch(opcao) {
                case 1:
                    new Thread(() -> tipos.Rajada(3, 3, 10000)).start();
                    break;
                case 2:
                    new Thread(() -> tipos.Padrao(5, 5000)).start();
                    break;
                case 3:
                    new Thread(() -> tipos.Aleatorio(5, 10000)).start();
                    break;
            }
        } while(opcao != 0);
    }
    
    public static void ImprimeOpcoes() {
        System.out.println("Digite a sua opcao");
        System.out.println("0 - Sair");
        System.out.println("1 - Rajada");
        System.out.println("2 - Padrao");
        System.out.println("3 - Aleatorio\n");
    }
}
