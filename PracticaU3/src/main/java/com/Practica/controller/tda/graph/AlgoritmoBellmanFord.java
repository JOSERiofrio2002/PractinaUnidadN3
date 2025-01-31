package com.Practica.controller.tda.graph;

import java.util.HashMap;
import java.util.Map;

public class AlgoritmoBellmanFord<E> extends GrahpLabelDirect<E> {
    private Float[] distancias;
    private Map<Integer, Integer> predecesores; 

    public AlgoritmoBellmanFord(Integer nro_vertices, Class<E> clazz) {
        super(nro_vertices, clazz);
    }
  
    private void inicializarDistancias(E raiz) throws Exception {
        int n = nro_Ver();
        distancias = new Float[n + 1];
        for (int i = 1; i <= n; i++) {
            distancias[i] = null; 
        }
        distancias[getVerticeL(raiz)] = 0f; 
    }

    public void calcularCaminosCortos(E raiz) throws Exception {
        inicializarDistancias(raiz);
        int n = nro_Ver();

       
        for (int i = 1; i < n; i++) {
            for (int u = 1; u <= n; u++) {
                for (int v = 1; v <= n; v++) {
                    if (is_Edge(u, v)) { 
                        Float peso = weight_edge(u, v); 
                        if (distancias[u] != null) { 
                            if (distancias[v] == null || distancias[u] + peso < distancias[v]) {
                                distancias[v] = distancias[u] + peso; 
                            }
                        }
                    }
                }
            }
        }

       
        for (int u = 1; u <= n; u++) {
            for (int v = 1; v <= n; v++) {
                if (is_Edge(u, v)) { 
                    Float peso = weight_edge(u, v);
                    if (distancias[u] != null && distancias[u] + peso < distancias[v]) {
                        throw new Exception("El grafo contiene un ciclo negativo.");
                    }
                }
            }
        }
    }

    public void imprimirCaminosCortos(E raiz) throws Exception {
        calcularCaminosCortos(raiz); 
        int n = nro_Ver();

        System.out.println("Distancias desde " + raiz + ":");
        for (int i = 1; i <= n; i++) {
            E destino = getLabelL(i);
            System.out.println("Hacia " + destino + ": " + (distancias[i] != null ? distancias[i] : "Infinito"));
        }
    }


    public Float obtenerCaminoCorto(E raiz, E destino) throws Exception {
        calcularCaminosCortos(raiz); 
        return distancias[getVerticeL(destino)];
    }

    public String reconstruirCamino(E raiz, E destino) throws Exception {
        int origen = getVerticeL(raiz);
        int destinoVertice = getVerticeL(destino);

        if (distancias[destinoVertice] == null) {
            return "No hay camino";
        }

        StringBuilder camino = new StringBuilder();
        int actual = destinoVertice;
        float distanciaTotal = 0;

        while (actual != -1) {
            if (predecesores.get(actual) != -1) {
                distanciaTotal += weight_edge(predecesores.get(actual), actual);
            }
            camino.insert(0, getLabelL(actual) + " -> ");
            actual = predecesores.get(actual);
        }
        camino.delete(camino.length() - 4, camino.length()); // Eliminar la última flecha

        System.out.println("Distancia total recorrida: " + distanciaTotal);
        return "Camino: " + camino.toString() + "\nDistancia total: " + distanciaTotal;
    }


    public Map<String, Object> obtenerDatosGrafo() {
        Map<String, Object> datosGrafo = new HashMap<>();
        datosGrafo.put("distancias", distancias);
        datosGrafo.put("predecesores", predecesores);
        return datosGrafo;
    }
}