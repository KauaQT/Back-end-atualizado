package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequestDto {

    private String comentario;
    private int Pontualidade;
    private int Comunicacao;
    private int Dirigibilidade;
    private int Seguranca;
    private int idMotorista;
    private int idUsuario;

}
