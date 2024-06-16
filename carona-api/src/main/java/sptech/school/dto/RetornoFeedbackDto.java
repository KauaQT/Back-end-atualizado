package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetornoFeedbackDto {
    private String comentario;
    private UsuarioListagemFeedbackDto usuario;
    private UsuarioListagemFeedbackDto motorista;
}
