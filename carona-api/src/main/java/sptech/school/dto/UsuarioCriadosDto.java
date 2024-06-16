package sptech.school.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import sptech.school.enity.Endereco;
import sptech.school.enity.Usuario;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioCriadosDto {


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

}
