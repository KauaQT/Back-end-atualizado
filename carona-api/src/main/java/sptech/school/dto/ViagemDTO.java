package sptech.school.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ViagemDTO {

    private Integer idCarro;
    private Integer idMotorista;
    private Double latitudePontoPartida;
    private Double longitudePontoPartida;
    private Double latitudePontoDestino;
    private Double longitudePontoDestino;
    private LocalDate diaViagem;
    private String horario;
    private Float valor;
    private Integer qntPassageiros;
    private Boolean soMulheres;
    private List<Integer> passageiros;

}
