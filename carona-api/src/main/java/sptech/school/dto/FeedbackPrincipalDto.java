package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.enity.CriteriosFeedback;
import sptech.school.enity.Feedback;

@Getter
@Setter
public class FeedbackPrincipalDto {

    private int idFeedback;
    private String comentario;
    private Integer quantidadeEstrelasPontualidade;
    private Integer quantidadeEstrelasComunicacao;
    private Integer quantidadeEstrelasDirigibilidade;
    private Integer quantidadeEstrelasSeguranca;
    private String nomeUsuario;
    private String nomeMotorista;

    public FeedbackPrincipalDto(Feedback feedback) {
        this.idFeedback = feedback.getId();
        this.comentario = feedback.getComentario();

        CriteriosFeedback criteriosFeedback = feedback.getCriteriosFeedback();
        if (criteriosFeedback != null) {
            this.quantidadeEstrelasPontualidade = criteriosFeedback.getQuantidadePontualidade();
            this.quantidadeEstrelasComunicacao = criteriosFeedback.getQuantidadeComunicacao();
            this.quantidadeEstrelasDirigibilidade = criteriosFeedback.getQuantidadeDirigibilidade();
            this.quantidadeEstrelasSeguranca = criteriosFeedback.getQuantidadeSeguranca();
        }

        if (feedback.getUsuario() != null) {
            this.nomeUsuario = feedback.getUsuario().getNome();
        }

        if (feedback.getMotorista() != null) {
            this.nomeMotorista = feedback.getMotorista().getNome();
        }
    }
}
