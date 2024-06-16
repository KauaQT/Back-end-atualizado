package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.enity.Carro;

import java.time.LocalDate;
import java.util.List;

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
