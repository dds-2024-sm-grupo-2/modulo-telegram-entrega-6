package ar.edu.utn.dds.k3003.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeladeraDTO {
    private String nombre;
    private Integer cantidadTotalDeViandas;
    private Integer ntiempo;
    private Integer tempMax;
    private Integer tempMin;

    public HeladeraDTO2(String nombre, Integer cantidadTotalDeViandas) {
        this.nombre=nombre;
        this.cantidadTotalDeViandas = cantidadTotalDeViandas;
    }
    public HeladeraDTO2(String nombre, Integer cantidadTotalDeViandas, Integer ntiempo, Integer tempMax, Integer tempMin) {
        this.nombre=nombre;
        this.cantidadTotalDeViandas = cantidadTotalDeViandas;
        this.ntiempo = ntiempo;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }
    public HeladeraDTO2(){}
}
