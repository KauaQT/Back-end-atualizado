package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackEspecificoDto {

    private String comentario;

    private Integer quantidadeEstrelasPontualidade;

    private Integer quantidadeEstrelasComunicacao;

    private Integer quantidadeEstrelasDirigibilidade;

    private Integer quantidadeEstrelasSeguranca;
}
