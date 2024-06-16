package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UsuarioListagemCarro {

    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private List<CarroDto> carros;
}