package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.Response.GeocodingResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RetornoViagemDto {

    private Integer idViagem;
    private Integer idCarro;

    private String  enderecoPartida;

    private String enderecoDestino;

    private LocalDate diaViagem;

    private String horario;

    private Float valor;

    private Integer qntPassageiros;

    private int tempoMedioViagem;

    private Boolean soMulheres;
}
