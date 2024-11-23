package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaColaboradores;
import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;
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

public class ColaboradoresProxy implements FachadaColaboradores {

    private final String endpoint;
    private final ColaboradoresRetrofitClient service;

    public ColaboradoresProxy(ObjectMapper objectMapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_COLABORADORES", "http://localhost:8084/");
        var retrofit = new Retrofit.Builder().baseUrl(this.endpoint).addConverterFactory(JacksonConverterFactory.create(objectMapper)).build();
        this.service = retrofit.create(ColaboradoresRetrofitClient.class);
    }

    public ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO getColab(Long id){
        try {
            Response<ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO> response = service.getColab(id).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener al colaborador: " + response.errorBody().string());
            }
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Error al obtener al colaborador: ", e);
        }
    }

    public void cambiarFormas(Long id, List<MisFormasDeColaborar> formas){
        try {
            Response<Void> response = service.cambiarFormas(id, formas).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo cambiar las formas: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cambiar las formas: ", e);
        }
    }

    public void reportarFalla(IncidenteDTO incidenteDTO) {
        try {
            Response<Void> response = service.reportarFalla(incidenteDTO).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo reportar a colaboradores la falla de la heladera: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al reportar falla en heladera: ", e);
        }
    }

    @Override
    public ColaboradorDTO agregar(ColaboradorDTO colaborador) {
        return null;
    }

    @Override
    public ColaboradorDTO buscarXId(Long colaboradorId) throws NoSuchElementException {
        return null;
    }

    @Override
    public Double puntos(Long colaboradorId) throws NoSuchElementException {
        return 0.0;
    }

    @Override
    public ColaboradorDTO modificar(Long colaboradorId, List<FormaDeColaborarEnum> formas) throws NoSuchElementException {
        return null;
    }

    @Override
    public void actualizarPesosPuntos(Double pesosDonados, Double viandasDistribuidas, Double viandasDonadas, Double tarjetasRepartidas, Double heladerasActivas) {

    }

    @Override
    public void setLogisticaProxy(FachadaLogistica logistica) {

    }

    @Override
    public void setViandasProxy(FachadaViandas viandas) {

    }
}