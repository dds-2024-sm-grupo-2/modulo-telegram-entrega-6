package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class LogisticaProxy{

    private final String endpoint;
    private final LogisticaRetrofitClient service;

    public LogisticaProxy(ObjectMapper objectMapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_LOGISTICA", "http://localhost:8085/");
        var retrofit = new Retrofit.Builder().baseUrl(this.endpoint).addConverterFactory(JacksonConverterFactory.create(objectMapper)).build();
        this.service = retrofit.create(LogisticaRetrofitClient.class);
    }

    public void nueva_ruta(Long colaboradorId, Integer heladeraIdOrigen, Integer heladeraIdDestino) {
        try {
            Response<Void> response = service.nueva_ruta(new RutaDTO(colaboradorId, heladeraIdOrigen, heladeraIdDestino)).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo crear la ruta: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear la ruta: ", e);
        }
    }

    public TrasladoDTO asignar_traslado(String qrVianda, Integer heladeraIdOrigen, Integer heladeraIdDestino) {
        try {
            Response<TrasladoDTO> response = service.asignar_traslado(new TrasladoDTO(qrVianda, heladeraIdOrigen, heladeraIdDestino)).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo asignar el traslado: " + response.errorBody().string());
            }
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Error al asignar el traslado: ", e);
        }
    }

    public void modificar_traslado(Long idTraslado, EstadoTrasladoEnum estadoTrasladoEnum){
        try {
            TrasladoDTO traslado = new TrasladoDTO(null,null,null);
            traslado.setStatus(estadoTrasladoEnum);
            Response<Void> response = service.cambiarFormas(idTraslado, traslado).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo modificar el estado del traslado " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cambiar las formas: ", e);
        }
    }
}
