package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import ar.edu.utn.dds.k3003.model.enums.EstadoIncidenteEnum;
import ar.edu.utn.dds.k3003.model.enums.TipoIncidenteEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class IncidentesProxy{

    private final String endpoint;
    private final IncidentesRetrofitClient service;

    public IncidentesProxy(ObjectMapper objectMapper) {
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_INCIDENTES", "http://localhost:8085/");
        var retrofit = new Retrofit.Builder().baseUrl(this.endpoint).addConverterFactory(JacksonConverterFactory.create(objectMapper)).build();
        this.service = retrofit.create(IncidentesRetrofitClient.class);
    }

    public void crearIncidente(Long heladeraId) {
        try {
            Response<Void> response = service.crearIncidente(new IncidenteDTO(TipoIncidenteEnum.FALLA_TECNICA, heladeraId, EstadoIncidenteEnum.ACTIVO, false, null, null)).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo crear el incidente: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el incidente: ", e);
        }
    }

    public void resolver_incidente(Long idIncidente){
        try {
            Response<Void> response = service.resolver_incidente(idIncidente, new IncidenteDTO(null,null, EstadoIncidenteEnum.RESUELTO,false,null,null)).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo resolver el incidente " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al cambiar las formas: ", e);
        }
    }
}
