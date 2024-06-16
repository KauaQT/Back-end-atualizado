package sptech.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cep;

    private String uf;

    private String rua;

    private String cidade;

    private String logradouro;

    private String bairro;

    private Integer numero;

    private Double lat;

    private Double lon;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;


}
