package com.Practica.controller.tda.graph;

import com.Practica.controller.excepcion.OverFlowException;
import com.Practica.controller.tda.list.LinkedList;

public class GrahpDirect extends Graph {
    private Integer nro_vertices;
    private Integer nro_edges;
    private LinkedList<Adycencia> listAdyacencias[];

     @SuppressWarnings("unchecked")
    public GrahpDirect(Integer nro_vertices) {
        this.nro_vertices = nro_vertices;
        this.nro_edges = 0;
        listAdyacencias = new LinkedList[nro_vertices + 1];
        for (int i = 1; i <= nro_vertices; i++) {
            listAdyacencias[i] = new LinkedList<>();

        }
    }

    @Override
    public Integer nro_Edges() {
        return this.nro_edges;
    }

    @Override
    public Integer nro_Ver() {
        return this.nro_vertices;
    }

    @Override
    public Boolean is_Edge(Integer v1, Integer v2) throws Exception {
        Boolean band = false;
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            LinkedList<Adycencia> lista = listAdyacencias[v1];
            if (!lista.isEmpty()) {
                Adycencia[] matrix = lista.toArray();
                for (int i = 0; i < matrix.length; i++) {
                    Adycencia aux = matrix[i];
                    if (aux.getDestination().intValue() == v2.intValue()) {
                        band = true;
                        break;

                    }

                }

            }
        } else {
            throw new OverFlowException("Los vertices están fuera de rango");
        }
        return band;
    }

    @Override
    public Float weight_edge(Integer v1, Integer v2) throws Exception {
        Float weight = Float.NaN;
        if (is_Edge(v1, v2)) {
            LinkedList<Adycencia> lista = listAdyacencias[v1];
            if (!lista.isEmpty()) {
                Adycencia[] matrix = lista.toArray();
                for (int i = 0; i < matrix.length; i++) {
                    Adycencia aux = matrix[i];
                    if (aux.getDestination().intValue() == v2.intValue()) {
                        weight = aux.getWeight();
                        break;

                    }

                }

            }
        } else {
            throw new OverFlowException("Los vertices están fuera de rango");
        }

        return weight;
    }

    @Override
    public void add_edge(Integer v1, Integer v2, Float weight) throws Exception {
        System.out.println("v1: " + v1 + " v2: " + v2);
        if (v1.intValue() <= nro_vertices && v2.intValue() <= nro_vertices) {
            if (!is_Edge(v1, v2)) {
                nro_edges++;
                Adycencia aux = new Adycencia();
                aux.setWeight(weight);
                aux.setDestination(v2);
                listAdyacencias[v1].add(aux);
            }
        } else {
            throw new OverFlowException("Los vertices están fuera de rango");
        }

    }

    @Override
    public void add_edge(Integer v1, Integer v2) throws Exception {
        this.add_edge(v1, v2, Float.NaN);

    }

    @Override
    public LinkedList<Adycencia> adyacencias(Integer v1) {
        return listAdyacencias[v1];
    }

    public void setNro_edges(Integer nro_edges) {
        this.nro_edges = nro_edges;
    }

    public LinkedList<Adycencia>[] getListAdyacencias() {
        return listAdyacencias;
    }

    public void setListaAdyancencias(LinkedList<Adycencia>[] listAdyacencias){

        this.listAdyacencias = listAdyacencias; 
    }
 


}