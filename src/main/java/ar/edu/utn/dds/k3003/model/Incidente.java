package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.enums.EstadoIncidenteEnum;
import ar.edu.utn.dds.k3003.model.enums.TipoIncidenteEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estadoIncidente")
    private EstadoIncidenteEnum estadoIncidente;

    @Column(name = "heladeraId")
    private Long heladeraId;

    @Column(name = "tipoIncidente")
    private TipoIncidenteEnum tipoIncidente;

    @Getter
    @Setter
    @Column(name = "excedeTemperatura")
    private boolean excedeTemperatura;

    @Column(name = "excesoTemperatura")
    private Integer excesoTemperatura;

    @Column(name = "tiempoSinRespuesta")
    private Integer tiempoSinRespuesta;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    public Incidente(EstadoIncidenteEnum estadoIncidente, TipoIncidenteEnum tipoIncidente, Long heladeraId, boolean excedeTemperatura, Integer excesoTemperatura, Integer tiempoSinRespuesta) {
        this.estadoIncidente = estadoIncidente;
        this.heladeraId = heladeraId;
        this.tipoIncidente = tipoIncidente;
        this.excedeTemperatura = excedeTemperatura;
        this.excesoTemperatura = excesoTemperatura;
        this.tiempoSinRespuesta = tiempoSinRespuesta;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Incidente() {
    }
}
