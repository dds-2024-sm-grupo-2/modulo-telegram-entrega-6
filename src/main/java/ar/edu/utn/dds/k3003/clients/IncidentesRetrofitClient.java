package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface IncidentesRetrofitClient {
    @POST("incidentes/{incidenteId}")
    Call<Void> resolver_incidente(@Path("incidenteId") Long incidenteId, @Body IncidenteDTO incidenteDTO);

}
