package sptech.school.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sptech.school.entity.Usuario;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioListagemDto {

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String genero;
    private Usuario.TipoUsuario tipoUsuario;

}
