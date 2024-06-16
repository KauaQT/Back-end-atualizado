package sptech.school.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnderecoDto {
    private int id;
    private String cep;
    private String uf;
    private String cidade;
    private String logradouro;
    private String bairro;
    private Integer numero;
    private String rua;
}
