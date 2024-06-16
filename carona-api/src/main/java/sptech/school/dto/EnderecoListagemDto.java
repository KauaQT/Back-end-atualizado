package sptech.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoListagemDto {

    private Integer id;
    private String cep;
    private String uf;
    private String cidade;
    private String logradouro;
    private String bairro;
    private Integer numero;
}
