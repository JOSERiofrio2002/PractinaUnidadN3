package com.Practica.rest;


import java.util.LinkedList;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
/* 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {
         HashMap mapa = new HashMap<>();
          AlgoritmoFloyd graph = new AlgoritmoFloyd<>(5, String.class);
        try {
            graph.labelsVertices(1, "Servicar");
            graph.labelsVertices(2, "Talleres Ordo�ez");
            graph.labelsVertices(3, "Talleres Faican");
            graph.labelsVertices(4, "Mecanica Automotriz Riofrio");
            graph.labelsVertices(5, "Talleres Jaramillo");

            graph.add_edge(1, 2, 5f);
            graph.add_edge(2, 3, 3f);
            graph.add_edge(3, 4, 10f);
            graph.add_edge(4, 5, 2f);
            
    
            graph.calcularCaminosCortos();;
            graph.imprimirMatrizDistancias();;

            System.out.println("Distancia mínima de Servicar a Talleres Ordo�ez: " + graph.obtenerDistanciaMinima("Servicar", "Talleres Ordo�ez"));
             


        } catch (Exception e) {
            System.out.println("Error MyResource " + e);
            mapa.put("msg", "Ok");
            mapa.put("data", e.toString());
            return Response.status(Status.BAD_REQUEST).entity(mapa).build();
        }
        System.out.println(graph.toString());
        mapa.put("msg", "OK");
        mapa.put("data", graph.toString());
        System.out.println(graph.toString());
        return Response.ok(mapa).build();


    }  

*/

    // Implementación del algoritmo de Floyd-Warshall
    public static void computeShortestPaths(int[][] adjacencyMatrix) {
        int vertices = adjacencyMatrix.length;
        int[][] distances = new int[vertices][vertices];

        // Inicializar la matriz de distancias
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                distances[i][j] = adjacencyMatrix[i][j];
            }
        }

        // Aplicar el algoritmo de Floyd-Warshall
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE
                            && distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }
    }

    // Implementación del algoritmo de Bellman-Ford
    public static void findShortestPaths(LinkedList<int[]> edges, int vertices, int source) {
        int[] distances = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[source] = 0;

        // Relajar las aristas repetidamente
        for (int i = 1; i < vertices; i++) {
            for (int j = 0; j < edges.size(); j++) {
                int[] edge = edges.get(j);
                int u = edge[0];
                int v = edge[1];
                int weight = edge[2];
                if (distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                }
            }
        }
    }

    // Método para medir el tiempo de ejecución de los algoritmos
    public static String measureExecutionTime(int[][] adjacencyMatrix, LinkedList<int[]> edges, int vertices) {
        long startTime, endTime;

        // Medir tiempo para Floyd-Warshall
        startTime = System.nanoTime();
        computeShortestPaths(adjacencyMatrix);
        endTime = System.nanoTime();
        long floydTime = endTime - startTime;

        // Medir tiempo para Bellman-Ford
        startTime = System.nanoTime();
        findShortestPaths(edges, vertices, 0);
        endTime = System.nanoTime();
        long bellmanTime = endTime - startTime;

        return "Tiempo de Floyd-Warshall: " + floydTime + " ns\n" +
               "Tiempo de Bellman-Ford: " + bellmanTime + " ns";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String executeAlgorithms() {
        // Crear grafos de diferentes tamaños
        int[][] smallGraph = generateRandomGraph(10);
        int[][] mediumGraph = generateRandomGraph(20);
        int[][] largeGraph = generateRandomGraph(30);

        LinkedList<int[]> smallEdges = extractEdges(smallGraph);
        LinkedList<int[]> mediumEdges = extractEdges(mediumGraph);
        LinkedList<int[]> largeEdges = extractEdges(largeGraph);

        // Medir tiempos y mostrar resultados
        StringBuilder result = new StringBuilder();
        result.append("Resultados para 10 nodos:\n");
        result.append(measureExecutionTime(smallGraph, smallEdges, 10)).append("\n");

        result.append("Resultados para 20 nodos:\n");
        result.append(measureExecutionTime(mediumGraph, mediumEdges, 20)).append("\n");

        result.append("Resultados para 30 nodos:\n");
        result.append(measureExecutionTime(largeGraph, largeEdges, 30)).append("\n");

        return result.toString();
    }

    // Método para generar un grafo aleatorio
    private int[][] generateRandomGraph(int size) {
        int[][] graph = new int[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    graph[i][j] = 0; // Distancia de un nodo a sí mismo es 0
                } else {
                    graph[i][j] = random.nextInt(10); // Distancia aleatoria entre 0 y 9
                }
            }
        }

        return graph;
    }

    // Método para extraer las aristas de un grafo
    private LinkedList<int[]> extractEdges(int[][] graph) {
        LinkedList<int[]> edges = new LinkedList<>();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] != 0) {
                    edges.add(new int[]{i, j, graph[i][j]});
                }
            }
        }
        return edges;
    }

}


