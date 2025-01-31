package com.Practica.controller.dao.services;

import com.Practica.controller.dao.TallerMecanicoDao;
import com.Practica.controller.tda.list.LinkedList;
import com.Practica.models.TallerMecanico;

public class TallerMecanicoServices {
    private TallerMecanicoDao obj;

    public TallerMecanicoServices() {
        this.obj = new TallerMecanicoDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    @SuppressWarnings("rawtypes")
    public LinkedList listAll() throws Exception {
        return obj.getListAll();
    }

    public TallerMecanico getTallerMecanico() {
        return obj.getTallerMecanico();
    }

    public void setTallerMecanico(TallerMecanico TallerMecanico) {
        obj.setTallerMecanico(TallerMecanico);
    }

    public TallerMecanico getTallerMecanicoByIndex(Integer index) throws Exception {
        return obj.getTallerMecanicoByIndex(index);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public Boolean delete(Integer index) throws Exception {
        return obj.deleteByIndex(index);
    }

    public String getTallerMecanicoJsonByIndex(Integer index) throws Exception {
        return obj.getTallerMecanicoJsonByIndex(index);
    }

    public TallerMecanico get(Integer id) throws Exception {
        return obj.get(id);
    }

    public LinkedList<TallerMecanico> order(String attribute, Integer type) throws Exception {
        return obj.order(attribute, type);
    }

    public void guardarGrafoEnJson() {
        obj.guardarGrafoEnJson();
    }

    public String generarMapa() {
        return obj.generarMapa();
    }

    public void inicializarGrafo() {
        obj.inicializarGrafo();
    }


 
}
