package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class MotoristaProximosDto {

    private String partida;

    private String destino;

    private LocalDate horario;
}
