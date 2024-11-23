package ar.edu.utn.dds.k3003.model.dtos;

import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;

import java.util.List;

public class FormasDeColaborarDTO {
    private List<MisFormasDeColaborar> formas;

    public List<MisFormasDeColaborar> getFormas() {
        return formas;
    }

    public void setFormas(List<MisFormasDeColaborar> formas) {
        this.formas = formas;
    }
}
