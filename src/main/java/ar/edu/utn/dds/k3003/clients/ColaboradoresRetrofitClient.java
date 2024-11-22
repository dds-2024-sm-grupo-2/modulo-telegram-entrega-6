package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface ColaboradoresRetrofitClient {

    @GET("colaboradores/{colaboradorID}")
    Call<ColaboradorDTO> getColab(@Path("colaboradorID") Long colaboradorID);

    @POST("fallas")
    Call<Void> reportarFalla(@Body IncidenteDTO incidenteDTO);
}