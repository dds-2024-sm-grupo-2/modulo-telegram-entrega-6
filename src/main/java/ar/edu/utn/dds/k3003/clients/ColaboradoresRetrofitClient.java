package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ColaboradoresRetrofitClient {

    @GET("colaboradores/{colaboradorID}")
    Call<ColaboradorDTO> getColab(@Path("colaboradorID") Long colaboradorID);

    @PATCH("colaboradores/{colabID}")
    Call<Void> cambiarFormas(@Path("colabID") Long id, @Body FormasDeColaborarDTO formas);

    @POST("fallas")
    Call<Void> reportarFalla(@Body IncidenteDTO incidenteDTO);

    @POST("arreglarHeladera/colaborador/{colabID}")
    Call<Void> arreglarHeladera(@Path("colabID") Long colabId, @Body IncidenteDTO incidenteDTO);
}