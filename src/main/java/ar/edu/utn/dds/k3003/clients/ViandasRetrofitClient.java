package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface ViandasRetrofitClient {

    @POST("viandas")
    Call<Void> agregar(@Body ViandaDTO viandaDTO);

    @GET("viandas/{qr}")
    Call<ViandaDTO> get(@Path("qr") String qr);

    @PATCH("viandas/{qr}/estado")
    Call<ViandaDTO> modificarEstado(@Path("qr") String qr, @Body EstadoViandaEnum estado);
}