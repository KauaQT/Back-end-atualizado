package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UsuarioCadastroRequest {

    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private String genero;
    private String tipoUsuario;
    private List<EnderecoListagemDto> enderecos;

}
