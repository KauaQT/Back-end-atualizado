package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private Integer idUsuario;
    private String email;
    private String nome;
    private String cpf;
    private String genero;
    private String dataNascimento;
    private String tipoUsuario;
    private String urlImagemUsuario;
}
