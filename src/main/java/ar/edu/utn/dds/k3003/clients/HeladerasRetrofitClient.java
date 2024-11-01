package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HeladerasRetrofitClient {

    @POST("cambiarEstadoActivo/{idHeladera}")
    Call<Void> modificarEstadoHeladera(@Path("idHeladera") Integer id);
}

