package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.model.dtos.*;
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

    @Override
    public void depositar(Integer heladeraId, String qrVianda) {

    }

    public void depositar2(ViandaRequest viandaRequest) {
        try {
            Response<Void> response = service.depositar2(viandaRequest).execute();
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

    public List<String> getRetirosDelDiaFormatted(List<RetiroDTO> retiros) {
        List<String> formattedRetiros = new ArrayList<>();
        for (RetiroDTO retiro : retiros) {
            formattedRetiros.add(formatRetiro(retiro));
        }
        return formattedRetiros;
    }

    private String formatRetiro(RetiroDTO retiro) {
        return "{ id=" + retiro.getId() +
                ", tarjeta='" + retiro.getTarjeta() + '\'' +
                ", heladeraId=" + retiro.getHeladeraId() + '}';
    }

    public List<String> getRetirosDelDia(Integer idHeladera) {
        Response<List<RetiroDTO>> response;
        try {
            response = service.getRetirosDelDia(idHeladera).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }

        return this.getRetirosDelDiaFormatted(response.body());
    }
    public void suscribirViandasDisponibles(SubscriptorDto sub) {
        Response<Void> response;
        try {
            response = service.suscribirViandasDisponibles(sub).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }
    }
    public void suscribirViandasFaltantes(SubscriptorDto sub) {
        Response<Void> response;
        try {
            response = service.suscribirViandasFaltantes(sub).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }

    }
    public void suscribirDesperfecto(SubscriptorDesperfectoDTO sub) {
        Response<Void> response;
        try {
            response = service.suscribirDesperfecto(sub).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }

    }
    public void desuscribirDesperfecto(DesSuscripcionDTO desub) {
        Response<Void> response;
        try {
            response = service.desuscribirDesperfecto(desub).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }
    }
    public void desuscribirViandasDisponibles(DesSuscripcionDTO desub) {
        Response<Void> response;
        try {
            response = service.desuscribirViandasDisponibles(desub).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }
    }
    public void desuscribirViandasFaltantes(DesSuscripcionDTO desub) {
        Response<Void> response;
        try {
            response = service.desuscribirViandasFaltantes(desub).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("No se pudo obtener las heladeras");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al depositar la vianda: ", e);
        }
    }


}