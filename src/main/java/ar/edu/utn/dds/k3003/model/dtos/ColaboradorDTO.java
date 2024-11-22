package ar.edu.utn.dds.k3003.model.dtos;

import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;

import java.util.List;

public class ColaboradorDTO {
    private Long id;
    private String nombre;
    private Double puntos = (double) 0;
    private List<MisFormasDeColaborar> formas;
    private Double dineroDonado;
    private Double heladerasReparadas;

    public void Colaborador() {}

    public void notificar(NotificacionDTO notificacionDTO){

        System.out.println("Mensaje Recibido: " + notificacionDTO.getMsg());

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPuntos() {
        return puntos;
    }

    public void setPuntos(Double puntos) {
        this.puntos = puntos;
    }

    public List<MisFormasDeColaborar> getFormas() {
        return formas;
    }

    public void setFormas(List<MisFormasDeColaborar> formas) {
        this.formas = formas;
    }

    public Double getDineroDonado() {
        return dineroDonado;
    }

    public void setDineroDonado(Double dineroDonado) {
        this.dineroDonado = dineroDonado;
    }

    public Double getHeladerasReparadas() {
        return heladerasReparadas;
    }

    public void setHeladerasReparadas(Double heladerasReparadas) {
        this.heladerasReparadas = heladerasReparadas;
    }
}
