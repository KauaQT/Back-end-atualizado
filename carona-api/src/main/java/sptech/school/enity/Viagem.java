package sptech.school.enity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "viagem")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viagem")
    private Integer idViagem;

    @Column(name = "dia_viagem")
    private LocalDate diaViagem;

    @Column(name = "horario_viagem")
    private String horarioViagem;

    @Column(name = "valor")
    private Float valor;

    @Column(name = "qnt_passageiros")
    private Integer qntPassageiros;

    @Column(name = "so_mulheres")
    private Boolean soMulheres;

    @Column(name = "tempo_medio_viagem")
    private int tempoMedioViagem;


    @Enumerated(EnumType.STRING)
    private StatusViagem status;

    public enum StatusViagem {
        PENDENTE,
        ANDAMENTO,
        FINALIZADA
    }

    @ManyToOne
    @JoinColumn(name = "fk_partida", referencedColumnName = "id_partida")
    private Partida partida;

    @ManyToOne
    @JoinColumn(name = "fk_destino")
    private Destino destino;

    @ManyToOne
    @JoinColumn(name = "fk_usuario")
    @JsonBackReference
    private Usuario motorista;

    @ManyToMany
    @JoinTable(
            name = "viagem_passageiros",
            joinColumns = @JoinColumn(name = "id_viagem"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> listaPassageiros = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "fk_carro")
    private Carro carro;

    public void addPassageiro(Usuario passageiro) {
        if (passageiro.equals(this.motorista)) {
            throw new IllegalArgumentException("O motorista não pode ser adicionado como passageiro na própria viagem.");
        }

        this.listaPassageiros.add(passageiro);
        passageiro.getViagens().add(this);
    }

}
