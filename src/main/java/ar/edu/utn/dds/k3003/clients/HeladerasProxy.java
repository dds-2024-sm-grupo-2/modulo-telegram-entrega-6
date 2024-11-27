package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import ar.edu.utn.dds.k3003.model.dtos.ViandaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class HeladerasProxy implements FachadaHeladeras {

    private final String endpoint;
    private final HeladerasRetrofitClient service;

    public HeladerasProxy(ObjectMapper objectMapper) {

        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_HELADERAS", "http://localhost:8082/");

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(HeladerasRetrofitClient.class);
    }

    public void modificarEstadoHeladera(IncidenteDTO incidenteDTO) {
        try {
            Response<Void> response = service.modificarEstadoHeladera(incidenteDTO.getHeladeraId().intValue()).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo cambiar estado de la heladera: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cambiar estado en heladera: ", e);
        }
    }

    @Override
    public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {
        return null;
    }


    public void depositar(Integer heladeraId, String qrVianda) {

        ViandaRequest viandaRequest = new ViandaRequest(heladeraId, qrVianda);
        try {
            Response<Void> response = service.depositar(viandaRequest).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo depositar la vianda: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }
    }

    public void retirar(RetiroDTO retiroDTO) {
        try {
            Response<Void> response = service.retirar(retiroDTO).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Error al retirar la vianda: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error de comunicación al retirar la vianda: ", e);
        }
    }

    @Override
    public Integer cantidadViandas(Integer integer) throws NoSuchElementException {
        return 0;
    }

    @Override
    public void temperatura(TemperaturaDTO temperaturaDTO) {

    }

    @Override
    public List<TemperaturaDTO> obtenerTemperaturas(Integer integer) {
        return List.of();
    }

    @Override
    public void setViandasProxy(FachadaViandas fachadaViandas) {

    }
    public List<HeladeraDTO> getHeladeras() {
        Response<List<HeladeraDTO>> response;
        try {
            response = service.getHeladeras().execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }

        return response.body();
    }

    public Integer getViandasDeHeladera(Integer idHeladera) {
        Response<Integer> response;
        try {
            response = service.getViandasDeHeladera(idHeladera).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }

        return response.body();
    }
}