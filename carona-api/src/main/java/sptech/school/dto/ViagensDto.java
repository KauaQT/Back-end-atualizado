package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.enity.Viagem;

import java.time.LocalDate;

@Getter
@Setter
public class ViagensDto {

    private Integer idViagem;
    private String origem;

    private String destino;

    private Float valor;

    private LocalDate dataViagem;

    private Viagem.StatusViagem status;
}
