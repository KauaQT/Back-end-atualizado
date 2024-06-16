package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.entity.Usuario;

import java.util.List;

@Getter
@Setter
public class CadastroDto {

    private Integer id;

    private String nome;

    private String email;

    private String cpf;

    private String senha;

    private String telefone;

    private Usuario.TipoUsuario tipoUsuario;

    private List<EnderecoListagemDto> enderecos;
}
