package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetalhesViagemDto {

    private String nomeMotorista;

    private Double quantidadeEstrelas;

    private String nomePartida;

    private String nomeDestino;

    private String inicioViagem;

    private String fimViagem;

    private Double tempoMedioViagem;

    private String nomeCarro;

    private String modeloCarro;

    private String corCarro;

    private Double latitudePontoPartida;
    private Double longitudePontoPartida;
    private Double latitudePontoDestino;
    private Double longitudePontoDestino;

    private String foto;
    private String placaCarro;

    private List<PassageiroDto> passageiros;
}
