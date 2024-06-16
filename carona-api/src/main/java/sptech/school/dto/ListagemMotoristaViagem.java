package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListagemMotoristaViagem {

    private Integer idViagem;
    private String nomeMotorista;
    private Double quantidadeEstrelas;
    private String inicioViagem;
    private String fimViagem;
    private Float valor;
    private Double distanciaPontoPartidaViagem;
    private Double distanciaPontoDestinoViagem;
}
