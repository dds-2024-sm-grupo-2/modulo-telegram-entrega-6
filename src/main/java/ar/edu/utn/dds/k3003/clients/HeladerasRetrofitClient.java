package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface HeladerasRetrofitClient {

    @POST("depositos")
    @FormUrlEncoded
    Call<Void> depositar(@Field("heladeraId") Integer heladeraId, @Field("viandaQR") String viandaQR); //revisar esto

    @POST("retiros")
    Call<Void> retirar(@Body RetiroDTO retiroDTO);

    @POST("cambiarEstadoActivo/{idHeladera}")
    Call<Void> modificarEstadoHeladera(@Path("idHeladera") Integer id);
}

