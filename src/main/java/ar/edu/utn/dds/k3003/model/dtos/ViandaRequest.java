package ar.edu.utn.dds.k3003.model.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ViandaRequest {
    private Integer heladeraId;
    private String qrVianda;

    public ViandaRequest(Integer heladeraId, String qrVianda) {
        this.heladeraId = heladeraId;
        this.qrVianda = qrVianda;
    }

    public ViandaRequest() {

    }

}