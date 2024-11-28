package ar.edu.utn.dds.k3003.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptorDesperfectoDTO {
    public Long idColaborador;
    public Integer idHeladera;

    public SubscriptorDesperfectoDTO(Long idColaborador, Integer idHeladera) {
        this.idColaborador = idColaborador;
        this.idHeladera = idHeladera;
    }
    public SubscriptorDesperfectoDTO() {}
}