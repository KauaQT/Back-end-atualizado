package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarroCadastroDto {
    private String marca;
    private String modelo;
    private String placa;
    private String cor;
    private String tipo;
    private String ano;
    private int usuarioId;
}
