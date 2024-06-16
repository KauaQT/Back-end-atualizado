package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;
import sptech.school.enity.Usuario;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PerfilDto {

   private String nome;

   private String cpf;

   private String email;

   private String genero;

   private LocalDate dataNascimento;

   private String cep;

   private Integer numero;

   private Usuario.TipoUsuario tipoUsuario;

   private Integer quantidadeViagens;

   private Integer quantidadePagamentos;
}
