package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
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

    public RutaDTO nueva_ruta(Long colaboradorId, Integer heladeraIdOrigen, Integer heladeraIdDestino) {
        try {
            RutaDTO ruta = new RutaDTO(colaboradorId, heladeraIdOrigen, heladeraIdDestino);
            Response<RutaDTO> response = service.nueva_ruta(ruta).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo crear la ruta: " + response.errorBody().string());
            }
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Error al crear la ruta: ", e);
        }
    }
}
