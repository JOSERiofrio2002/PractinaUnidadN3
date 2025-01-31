package com.Practica.controller.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.Practica.controller.dao.implement.AdapterDao;
import com.Practica.controller.tda.graph.Adycencia;
import com.Practica.controller.tda.graph.GrapLabelNoDirect;
import com.Practica.controller.tda.list.LinkedList;
import com.Practica.models.TallerMecanico;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TallerMecanicoDao extends AdapterDao<TallerMecanico> {
    private TallerMecanico TallerMecanico;
    private LinkedList<TallerMecanico> listAll;
    private Gson g = new Gson();
    private GrapLabelNoDirect<TallerMecanico> graph;

    public TallerMecanicoDao() {
        super(TallerMecanico.class);
    }

    public TallerMecanico getTallerMecanico() {
        if (TallerMecanico == null) {
            TallerMecanico = new TallerMecanico();
        }
        return TallerMecanico;
    }

    public void setTallerMecanico(TallerMecanico TallerMecanico) {
        this.TallerMecanico = TallerMecanico;
    }

    public LinkedList<TallerMecanico> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = listAll().getSize() + 1;
        TallerMecanico.setId(id);
        this.persist(this.TallerMecanico);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getTallerMecanico(), getTallerMecanico().getId() - 1);
        this.listAll = listAll();
        return true;
    }

    public Boolean deleteByIndex(Integer id) throws Exception {
        this.delete(id - 1);
        LinkedList<TallerMecanico> list = listAll();
        for (int i = 0; i < list.getSize(); i++) {
            list.get(i).setId((i + 1));
        }
        updateListFile(list);
        this.listAll = list;
        return true;
    }
    

    public LinkedList<TallerMecanico> order(String atribute, Integer type) throws Exception {
        LinkedList<TallerMecanico> listita = listAll();
        if (!listita.isEmpty()) {
            // Llamamos al método genérico `order` de la LinkedList
            listita = listita.order(atribute, type);
        }
        return listita;
    }

    public TallerMecanico getTallerMecanicoByIndex(Integer Index) throws Exception {
        return get(Index);
    }

    public String getTallerMecanicoJsonByIndex(Integer Index) throws Exception {
        return g.toJson(get(Index));
    }

    public void setListAll(LinkedList<TallerMecanico> listAll) {
        this.listAll = listAll;
    }

    

    public String generarMapa() {
        StringBuilder mapa = new StringBuilder();
        mapa.append("var osmUrl = 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',\n");
        mapa.append("    osmAttrib = '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors',\n");
        mapa.append("    osm = L.tileLayer(osmUrl, {maxZoom: 15, attribution: osmAttrib});\n");
        mapa.append("var map = L.map('map').setView([-3.996716943365505, -79.20174975448631], 15);\n\n");
        mapa.append("L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {\n");
        mapa.append("    attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'\n");
        mapa.append("}).addTo(map);\n");

        try {
            for (int i = 0; i < listAll().getSize(); i++) {
                TallerMecanico taller = (TallerMecanico) listAll().get(i);
                mapa.append("L.marker([").append(taller.getLatitude()).append(", ").append(taller.getLongitude()).append("]).addTo(map)\n");
                mapa.append("    .bindPopup('").append(taller.getNombre()).append("')\n");
                mapa.append("    .openPopup();\n");
            }
            FileWriter fileWriter = new FileWriter("resources" + File.separator + "maps" + File.separator + "mapa.js");
            fileWriter.write(mapa.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapa.toString();
    }

    public void inicializarGrafo() {
        try {
            LinkedList<TallerMecanico> lv = listAll();
            if (!lv.isEmpty()) {
                graph = new GrapLabelNoDirect<>(lv.getSize(), TallerMecanico.class);
                TallerMecanico[] m = lv.toArray();

                // Etiquetar los vértices con las veterinarias
                for (Integer i = 0; i < lv.getSize(); i++) {
                    graph.labelsVertices((i + 1), m[i]);
                }

                generarAdyacenciasAleatorias();
            }
            if (graph != null) {
                generarMapa();
                graph.drawGraph();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generarAdyacenciasAleatorias() {
        int numVertices = graph.nro_Ver();
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 0; j < 3; j++) {
                int destino = (int) (Math.random() * numVertices) + 1;
                float peso = (float) (Math.random() * 10) + 1;
                try {
                    graph.add_edge(i, destino, peso);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Double calcularDistancia(Double lat1, Double lon1, Double lat2, Double lon2) {
        final Integer R = 6371; // Radio de la Tierra en kilómetros
        double latDist = Math.toRadians(lat2 - lat1);
        double lonDist = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDist / 2) * Math.sin(latDist / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDist / 2) * Math.sin(lonDist / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Retorna la distancia en kilómetros
    }

    private void crearAdyacenciasParaNuevoNodo(Integer idNodo, Integer totalVertices) {
        Random rand = new Random();
        int adyacenciasGeneradas = 0;

        while (adyacenciasGeneradas < 3) {
            int destino = rand.nextInt(totalVertices) + 1;
            if (destino == idNodo) {
                continue; // Evitar autoreferencia
            }

            try {
                if (!graph.is_Edge(idNodo, destino)) {
                    TallerMecanico origen = graph.getLabelL(idNodo);
                    TallerMecanico destinoTaller = graph.getLabelL(destino);

                    double distancia = calcularDistancia(
                            origen.getLatitude(), origen.getLongitude(),
                            destinoTaller.getLatitude(), destinoTaller.getLongitude());

                    graph.add_edge(idNodo, destino, (float) distancia);
                    adyacenciasGeneradas++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void guardarGrafoEnJson() {
        if (graph == null) {
            inicializarGrafo(); 
        }

        JsonArray jsonGrafo = new JsonArray();
        File file = new File("media" + File.separator + "grafo.json");

        try {
            // Leer el archivo JSON existente si existe
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                jsonGrafo = g.fromJson(reader, JsonArray.class);
                reader.close();
            }

            // Crear un mapa para evitar duplicados (ID -> JsonObject)
            Map<Integer, JsonObject> existingNodesMap = new HashMap<>();
            for (int i = 0; i < jsonGrafo.size(); i++) {
                JsonObject nodo = jsonGrafo.get(i).getAsJsonObject();
                int id = nodo.get("id").getAsInt();
                existingNodesMap.put(id, nodo);
            }

            // Procesar los nodos actuales del grafo
            int numVertices = graph.nro_Ver();

            for (int i = 1; i <= numVertices; i++) {
                TallerMecanico taller = graph.getLabelL(i);

                // Si el nodo ya existe en el JSON, no modificarlo
                if (existingNodesMap.containsKey(taller.getId())) {
                    continue;
                }

                // Si es un nuevo nodo, generar sus adyacencias
                JsonObject nodo = new JsonObject();
                nodo.addProperty("id", taller.getId());
                nodo.addProperty("nombre", taller.getNombre());

                JsonArray adyacenciasArray = new JsonArray();
                LinkedList<Adycencia> adyacencias = graph.adyacencias(i);

                // Generar 3 adyacencias aleatorias para el nuevo nodo
                if (adyacencias.getSize() < 3) {
                    crearAdyacenciasParaNuevoNodo(taller.getId(), numVertices);
                    adyacencias = graph.adyacencias(i); // Actualizar adyacencias después de generarlas
                }

                // Guardar las adyacencias del nuevo nodo
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adycencia ady = adyacencias.get(j);
                    adyacenciasArray.add(ady.getDestination());
                }

                nodo.add("adyacencias", adyacenciasArray);
                jsonGrafo.add(nodo);
            }

            // Escribir el JSON actualizado
            FileWriter writer = new FileWriter(file);
            g.toJson(jsonGrafo, writer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getGrafoJson() {
        if (graph == null) {
            inicializarGrafo();
        }

        JsonObject jsonGraph = new JsonObject();
        JsonArray jsonVertices = new JsonArray();
        JsonArray jsonAristas = new JsonArray();

        try {
            // Procesar los vértices del grafo
            for (int i = 1; i <= graph.nro_Ver(); i++) {
                TallerMecanico taller = graph.getLabelL(i);
                JsonObject jsonVertice = new JsonObject();
                jsonVertice.addProperty("id", taller.getId());
                jsonVertice.addProperty("nombre", taller.getNombre());
                jsonVertice.addProperty("latitude", taller.getLatitude());
                jsonVertice.addProperty("longitude", taller.getLongitude());
                jsonVertices.add(jsonVertice);
            }

            // Procesar las aristas del grafo
            for (int i = 1; i <= graph.nro_Ver(); i++) {
                LinkedList<Adycencia> adyacencias = graph.adyacencias(i);
                for (int j = 0; j < adyacencias.getSize(); j++) {
                    Adycencia ady = adyacencias.get(j);
                    JsonObject jsonArista = new JsonObject();
                    jsonArista.addProperty("source", i);
                    jsonArista.addProperty("target", ady.getDestination());
                    jsonArista.addProperty("weight", ady.getWeight());
                    jsonAristas.add(jsonArista);
                }
            }

            jsonGraph.add("vertices", jsonVertices);
            jsonGraph.add("aristas", jsonAristas);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return g.toJson(jsonGraph);
    }


    
    
}
