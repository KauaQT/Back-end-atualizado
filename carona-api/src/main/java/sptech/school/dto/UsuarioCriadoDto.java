package sptech.school.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import sptech.school.entity.Endereco;
import sptech.school.entity.Usuario;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioCriadoDto {

    private String nome;

    @CPF
    private String cpf;

    @Email
    private String email;

    private String senha;

    @Past
    private LocalDate dataNascimento;

    private String genero;

    @NotNull
    private Usuario.TipoUsuario tipoUsuario;

    @NotNull
    private Endereco endereco;

    private String urlImagemUsuario; // Novo campo para a URL da imagem

}
