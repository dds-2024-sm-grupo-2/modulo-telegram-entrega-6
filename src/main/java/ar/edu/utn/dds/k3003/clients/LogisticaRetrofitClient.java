package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.model.dtos.ColaboradorDTO;
import ar.edu.utn.dds.k3003.model.dtos.FormasDeColaborarDTO;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface LogisticaRetrofitClient {

    @POST("rutas")
    Call<Void> nueva_ruta(@Body RutaDTO rutaDTO);

    @POST("traslados")
    Call<TrasladoDTO> asignar_traslado(@Body TrasladoDTO trasladoDTO);

    @PATCH("traslados/{trasladoId}")
    Call<Void> cambiarFormas(@Path("trasladoId") Long id, @Body TrasladoDTO trasladoDTO);
}
