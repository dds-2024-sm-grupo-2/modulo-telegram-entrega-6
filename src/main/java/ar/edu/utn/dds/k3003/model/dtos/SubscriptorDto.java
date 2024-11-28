package ar.edu.utn.dds.k3003.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubscriptorDto {
    private Long idColaborador;
    private Integer idHeladera;
    private Integer nviandas;

    public SubscriptorDto(Long idColab,Integer idHeladera, Integer nviandas) {
        this.idColaborador=idColab;
        this.idHeladera=idHeladera;
        this.nviandas=nviandas;
    }
    public SubscriptorDto(){

    }



}