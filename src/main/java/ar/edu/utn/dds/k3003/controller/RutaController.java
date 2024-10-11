package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Collections;

public class RutaController {

    private final Fachada fachada;

    public RutaController(Fachada fachada) {
        this.fachada = fachada;
    }

    public void agregar(Context context) {

        var rutaDTO = context.bodyAsClass(RutaDTO.class);
        var rutaDTORta = this.fachada.agregar(rutaDTO);
        context.json(rutaDTORta);
        context.status(HttpStatus.CREATED);
    }

    public void eliminar(Context ctx) throws Exception {
        try {
            fachada.rutaRepository.borrarTodo();
            // Agrego un body en json diciendo que se eliminaron todas las rutas
            ctx.json(Collections.singletonMap("message", "Se eliminaron todas las rutas"));
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }
}