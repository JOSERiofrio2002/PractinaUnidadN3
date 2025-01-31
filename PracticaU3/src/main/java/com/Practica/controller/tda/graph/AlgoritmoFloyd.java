package com.Practica.controller.tda.graph;

import java.util.ArrayList;
import java.util.List;



public class AlgoritmoFloyd <E> extends GrahpLabelDirect<E> {
    private Float[][] matriz;
    private int origen;
    private int destino;
    private float [][] distancias;
    private int [][] siguiente;



    public AlgoritmoFloyd(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices, clazz);
        matriz = new Float[nro_vertices + 1][nro_vertices + 1];
    }

    private void inicializarMatriz() throws Exception {
        int n = nro_Ver();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i == j) {
                    matriz[i][j] = 0f; 
                } else if (is_Edge(i, j)) {
                    matriz[i][j] = weight_edge(i, j); 
                    matriz[i][j] = null; 
                }
            }
        }
    }

    public void calcularCaminosCortos() throws Exception {
        inicializarMatriz();
        int n = nro_Ver();
        for (int k = 1; k <= n; k++) { 
            for (int i = 1; i <= n; i++) { 
                for (int j = 1; j <= n; j++) { 
                    if (matriz[i][k] != null && matriz[k][j] != null) {
                        if (matriz[i][j] == null || matriz[i][k] + matriz[k][j] < matriz[i][j]) {
                            matriz[i][j] = matriz[i][k] + matriz[k][j]; 
                        }
                    }
                }
            }
        }
    }

    public Float obtenerDistanciaMinima(E origen, E destino) throws Exception {
        int i = getVerticeL(origen);
        int j = getVerticeL(destino);
        return matriz[i][j];
    }

    public void imprimirMatrizDistancias() throws Exception {
        int n = nro_Ver();
        System.out.println("Matriz de distancias más cortas:");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (matriz[i][j] == null) {
                    System.out.print("Null\t");
                } else {
                    System.out.print(matriz[i][j] + "\t");
                }
            }
            System.out.println();
        }
    
    }

    public List<List<Float>> obtenerMatrizDistancias() {
        int n = nro_Ver();
        List<List<Float>> matrizJSON = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            List<Float> fila = new ArrayList<>();
            for (int j = 1; j <= n; j++) {
                fila.add(matriz[i][j]);  
            }
            matrizJSON.add(fila);
        }
        return matrizJSON;
    }

    private String reconstruirCamino(int origen, int destino) {
        if (siguiente[origen][destino] == -1) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = origen;
        float distanciaTotal = 0; 

        while (actual != destino) {
            if (siguiente[actual][destino] == -1) {
                return "Error: Camino interrumpido inesperadamente.";
            }
          
            camino.append(actual).append(" -> ");
            distanciaTotal += distancias[actual][siguiente[actual][destino]];  // Sumar la distancia total
            actual = siguiente[actual][destino];
        }
        camino.append(destino); 
        distanciaTotal += distancias[actual][destino];  


        System.out.println("Distancia total recorrida: " + distanciaTotal);

        return "Camino: " + camino.toString() 
                + "\nDistancia total: " + distanciaTotal;

    }

    public void agregarArista(Integer v1, Integer v2, Float peso) {
        matriz[v1][v2] = peso;
    }


}
