package ar.edu.utn.dds.k3003.model.dtos;

import java.util.List;

public class NotificacionDTO {
    private Long idNotificacion;
    List<Long> idColab;
    String msg;

    public List<Long> getIdColab() {
        return idColab;
    }

    public void setIdColab(List<Long> idColab) {
        this.idColab = idColab;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
