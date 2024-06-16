package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BuscarViagemDto {

    private Double latitudePartida;

    private Double longitudePartida;

    private Double latitudeDestino;

    private Double longitudeDestino;

    private LocalDate diaViagem;

}
