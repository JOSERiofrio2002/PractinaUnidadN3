package com.Practica.rest;


import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.Practica.controller.dao.TallerMecanicoDao;
import com.Practica.controller.dao.services.TallerMecanicoServices;
import com.Practica.controller.excepcion.ListEmptyException;
import com.Practica.controller.tda.list.LinkedList;
import com.Practica.models.TallerMecanico;
import com.google.gson.Gson;


@Path("/taller")
public class TallerMecanicoApi {

    private TallerMecanicoDao tallerMecanicoDao = new TallerMecanicoDao();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap map = new HashMap<>();
        TallerMecanicoServices ps = new TallerMecanicoServices();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }
        return Response.ok(map).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/order/{attribute}/{type}")
    public Response getOrder(@PathParam("attribute") String attribute, @PathParam("type") Integer type) {
        HashMap<String, Object> map = new HashMap<>();
        TallerMecanicoServices ps = new TallerMecanicoServices();

        try {
            // Llamar al nuevo método 'order' en el servicio
            LinkedList<TallerMecanico> lista = ps.order(attribute, type);

            map.put("msg", "OK");
            map.put("data", lista.toArray());

            if (lista.isEmpty()) {
                map.put("data", new Object[] {});
            }
        } catch (Exception e) {
            map.put("msg", "Error");
            map.put("data", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }

        return Response.ok(map).build();
    }


    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTallerMecanico(@PathParam("id") Integer id) {
        HashMap map = new HashMap<>();
        TallerMecanicoServices ps = new TallerMecanicoServices();
        try {
            ps.setTallerMecanico(ps.get(id));
        } catch (Exception e) {

        }

        map.put("msg", "OK");
        map.put("data", ps.getTallerMecanico());
        if (ps.getTallerMecanico().getId() == null) {
            map.put("data", "No existe la persona con ese identificador");
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }
        return Response.ok(map).build();
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {

        HashMap res = new HashMap<>();
        Gson g = new Gson();
        String a = g.toJson(map);
        System.out.println("Datos recibidos: " + a);

        try {
            TallerMecanicoServices ps = new TallerMecanicoServices();
            ps.getTallerMecanico().setNombre(map.get("nombre").toString());
            ps.getTallerMecanico().setDescripcion(map.get("descripcion").toString());
            ps.getTallerMecanico().setLatitude(Double.parseDouble(map.get("latitud").toString()));
            ps.getTallerMecanico().setLongitude(Double.parseDouble(map.get("longitud").toString()));
            ps.getTallerMecanico().setTelefono(map.get("telefono").toString());
            ps.getTallerMecanico().setHorario(map.get("horario").toString());
            ps.save();
            res.put("msg", "OK");
            res.put("data", "Taller Mecanico registrado correctamente");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Error en sav data: " + e.toString());
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap map) {
        HashMap res = new HashMap<>();

        try {
            TallerMecanicoServices ps = new TallerMecanicoServices();
            ps.setTallerMecanico(ps.get(Integer.parseInt(map.get("id").toString())));
            if (ps.getTallerMecanico().getId() == null) {
                res.put("msg", "error");
                res.put("data", "Error no existe el Taller Mecanico");
                return Response.status(Status.BAD_REQUEST).entity(res).build();
            } else {
                ps.getTallerMecanico().setNombre(map.get("nombre").toString());
                ps.update();
                res.put("status", "success");
                res.put("message", "Taller Mecanico editado con éxito.");
                return Response.ok(res).build();
            }
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

     @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRutaByIndex(@PathParam("id") Integer id) {
        TallerMecanicoServices ps = new TallerMecanicoServices();
        HashMap map = new HashMap<>();

        try {
            if (ps.delete(id)) {
                map.put("status", "success");
                map.put("message", "Taller mecanico  eliminada con éxito.");
                return Response.ok(map).build();
            } else {
                map.put("status", "error");
                map.put("message", "No se pudo eliminar el taller mecanico.");
                return Response.ok(map).build();
            }
        } catch (Exception e) {
            map.put("status", "error");
            map.put("message", "Error interno del servidor: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    
    @Path("/guardargrafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarGrafo() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            TallerMecanicoServices ps = new TallerMecanicoServices();
            ps.guardarGrafoEnJson(); // Llama al método para guardar el grafo en JSON
            res.put("msg", "OK");
            res.put("data", "Grafo guardado correctamente en JSON");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/creargrafo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response grafoVerAdmin() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            com.Practica.controller.dao.TallerMecanicoDao TallerMecanicoDao = new TallerMecanicoDao();
            LinkedList<TallerMecanico> listaTallerMecanico = TallerMecanicoDao.getListAll();         
            TallerMecanicoDao.inicializarGrafo();;
            TallerMecanicoDao.guardarGrafoEnJson(); 
            res.put("msg", "Grafo generado exitosamente");
            res.put("lista", listaTallerMecanico.toArray());
            return Response.ok(res).build();   
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
        }
    } 

    @GET
    @Path("/mostrargrafo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGrafo() {
        TallerMecanicoDao tallerMecanicoDao = new TallerMecanicoDao();
        String grafoJson = tallerMecanicoDao.getGrafoJson();
        return Response.ok(grafoJson).build();
    }
    

}
