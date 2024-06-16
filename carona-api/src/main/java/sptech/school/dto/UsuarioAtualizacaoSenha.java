package sptech.school.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioAtualizacaoSenha {
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String genero;
    private LocalDate dataNascimento;
    private String  CodigoRecuperacaoSenha;
}
