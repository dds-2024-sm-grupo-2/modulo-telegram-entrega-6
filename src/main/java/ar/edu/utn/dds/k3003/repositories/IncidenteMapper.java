package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.dtos.IncidenteDTO;

public class IncidenteMapper {

    public IncidenteDTO map(Incidente incidente){
        IncidenteDTO incidenteDTO = new IncidenteDTO(
                incidente.getTipoIncidente(),
                incidente.getHeladeraId(),
                incidente.getEstadoIncidente(),
                incidente.isExcedeTemperatura(),
                incidente.getExcesoTemperatura(),
                incidente.getTiempoSinRespuesta()
        );

        incidenteDTO.setFechaCreacion(incidente.getFechaCreacion());
        incidenteDTO.setId(incidente.getId());

        return incidenteDTO;
    }
}