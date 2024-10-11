package ar.edu.utn.dds.k3003.model.dtos;

import ar.edu.utn.dds.k3003.model.enums.EstadoIncidenteEnum;
import ar.edu.utn.dds.k3003.model.enums.TipoIncidenteEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncidenteDTO {

    private Long id;
    private TipoIncidenteEnum tipoIncidente;
    private Long heladeraId;
    private EstadoIncidenteEnum estadoIncidente;
    private boolean excedeTemperatura;
    private Integer excesoTemperatura;
    private Integer tiempoSinRespuesta;
    private LocalDateTime fechaCreacion;

    public IncidenteDTO(TipoIncidenteEnum tipoIncidente, Long heladeraId, EstadoIncidenteEnum estadoIncidente, boolean excedeTemperatura, Integer excesoTemperatura, Integer tiempoSinRespuesta) {
        this.tipoIncidente = tipoIncidente;
        this.heladeraId = heladeraId;
        this.estadoIncidente = estadoIncidente;
        this.excedeTemperatura = excedeTemperatura;
        this.excesoTemperatura = excesoTemperatura;
        this.tiempoSinRespuesta = tiempoSinRespuesta;
    }
}