package ar.edu.utn.dds.k3003.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "colaboradorId")
    private Long colaboradorId;
    @Column(name = "heladeraIdOrigen")
    private Integer heladeraIdOrigen;
    @Column(name = "heladeraIdDestino")
    private Integer heladeraIdDestino;
    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "activo")
    private Boolean activo;
    @OneToMany(mappedBy = "ruta")
    private List<Traslado> traslados;

    public Ruta(Long colaboradorId, Integer heladeraIdOrigen, Integer heladeraIdDestino) {
        this.colaboradorId = colaboradorId;
        this.heladeraIdOrigen = heladeraIdOrigen;
        this.heladeraIdDestino = heladeraIdDestino;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    public Ruta() {
    }
}