package sptech.school.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CriteriosFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer quantidadeEstrelas;
    private Integer quantidadePontualidade;
    private Integer quantidadeComunicacao;
    private Integer quantidadeDirigibilidade;
    private Integer quantidadeSeguranca;
}
