package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface LogisticaRetrofitClient {
    @GET("colaboradores/{colaboradorID}")
    Call<ColaboradorDTO> getColab(@Path("colaboradorID") Long colaboradorID);

    @PATCH("colaboradores/{colabID}")
    Call<Void> cambiarFormas(@Path("colabID") Long id, @Body FormasDeColaborarDTO formas);
    @POST("fallas")
    Call<Void> reportarFalla(@Body IncidenteDTO incidenteDTO);
}