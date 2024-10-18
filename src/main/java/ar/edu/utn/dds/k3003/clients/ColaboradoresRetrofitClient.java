package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface ColaboradoresRetrofitClient {

    @POST("fallas")
    Call<Void> reportarFalla(@Body HeladeraDTO heladeraDTO);
}