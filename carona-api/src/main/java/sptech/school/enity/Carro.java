package sptech.school.enity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sptech.school.enity.Usuario;

@Entity
@Table(name = "carro")
@Getter
@Setter
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "cor")
    private String cor;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "placa")
    private String placa;

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    @JsonBackReference
    private Usuario usuario;
}
