package sptech.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Usuario motorista;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "criterios_feedback_id")
    private CriteriosFeedback criteriosFeedback;
}
