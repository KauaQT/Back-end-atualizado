package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.entity.Endereco;

@Getter
@Setter
public class LoginDto {
    private Integer idUsuario;
    private String email;
    private String nome;
    private String cpf;
    private String genero;
    private String dataNascimento;
    private String tipoUsuario;
    private String urlImagemUsuario;
    private Endereco endereco;
}
