package ar.edu.utn.dds.k3003.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DesSuscripcionDTO {
    private Integer idHeladera;
    private Long idColab;

    public DesSuscripcionDTO(Integer idHeladera, Long idColab) {
        this.idHeladera = idHeladera;
        this.idColab = idColab;
    }
    public DesSuscripcionDTO(){
    }

}
