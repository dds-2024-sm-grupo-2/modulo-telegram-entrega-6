package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.model.dtos.ViandaRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface HeladerasRetrofitClient {

    @POST("depositos")
    Call<Void> depositar2(@Body ViandaRequest viandaRequest); //revisar esto

    @POST("retiros")
    Call<Void> retirar(@Body RetiroDTO retiroDTO);

    @POST("cambiarEstadoActivo/{idHeladera}")
    Call<Void> modificarEstadoHeladera(@Path("idHeladera") Integer id);

    @GET("/heladeras")
    Call <List<HeladeraDTO>> getHeladeras();
}

