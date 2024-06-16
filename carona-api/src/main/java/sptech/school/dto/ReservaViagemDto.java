package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservaViagemDto {

    private Integer idViagem;
    private LocalDate horario;
    private Float valor;
    private Integer qntPassageiros;
    private Boolean soMulheres;
    private int tempoMedioViagem;
    private String pontoPartida;
    private String pontoDestino;
    private String motorista;
    private String marcaCarro;
    private String modeloCarro;
    private String placaCarro;
    private List<String> passageiros;
}
