package sptech.school.enity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "destino")
@ToString
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_destino")
    private Integer idDestino;

    @Column(name = "ponto_destino")
    private String pontoDestino;

}
