package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "traslados")
public class Traslado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "qrVianda")
    private String qrVianda;
    @ManyToOne
    @JoinColumn(name = "ruta_id")
    private Ruta ruta;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTrasladoEnum estado;
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "fechaTraslado")
    private LocalDateTime fechaTraslado;
    @Column(name = "collaboratorId")
    private Long collaboratorId;

    public Traslado(String qrVianda, Ruta ruta, EstadoTrasladoEnum estado, LocalDateTime fechaTraslado) {
        this.qrVianda = qrVianda;
        this.ruta = ruta;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaTraslado = fechaTraslado;
        this.collaboratorId = ruta.getColaboradorId();
    }

    public Traslado() {
    }
}