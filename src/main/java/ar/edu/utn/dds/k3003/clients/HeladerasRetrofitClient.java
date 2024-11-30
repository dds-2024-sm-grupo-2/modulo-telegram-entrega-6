package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.model.dtos.DesSuscripcionDTO;
import ar.edu.utn.dds.k3003.model.dtos.SubscriptorDesperfectoDTO;
import ar.edu.utn.dds.k3003.model.dtos.SubscriptorDto;
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
    
    @GET("/viandas/{idHeladera}")
    Call <Integer> getViandasDeHeladera(@Path("idHeladera") Integer idHeladera);

    @GET ("/retiros/{idHeladera}")
    Call <List<RetiroDTO>> getRetirosDelDia(@Path("idHeladera") Integer idHeladera);

    @POST ("/suscribirviandasdisponibles")
    Call <Void> suscribirViandasDisponibles(@Body SubscriptorDto subDto);

    @POST ("/suscribirviandasfaltantes")
    Call <Void> suscribirViandasFaltantes(@Body SubscriptorDto sub);

    @POST ("/suscribirdesperfecto")
    Call <Void> suscribirDesperfecto(@Body SubscriptorDesperfectoDTO sub);

    @POST ("/desuscribirviandasdisponibles")
    Call <Void> desuscribirViandasDisponibles(@Body DesSuscripcionDTO desub);

    @POST ("/desuscribirviandasfaltantes")
    Call <Void> desuscribirViandasFaltantes(@Body DesSuscripcionDTO desub);

    @POST ("/desuscribirdesperfecto")
    Call <Void> desuscribirDesperfecto(@Body DesSuscripcionDTO desub);
}

