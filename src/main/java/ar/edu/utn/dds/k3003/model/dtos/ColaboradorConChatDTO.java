package ar.edu.utn.dds.k3003.model.dtos;

import ar.edu.utn.dds.k3003.model.enums.MisFormasDeColaborar;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColaboradorConChatDTO {

    private String nombre;
    private List<MisFormasDeColaborar> formas;
    private Long chatID;

    public ColaboradorConChatDTO(String nombre, List<MisFormasDeColaborar> formas, Long chatID){
        this.nombre = nombre;
        this.formas = formas;
        this.chatID = chatID;
    }

    public ColaboradorConChatDTO(){}

}
