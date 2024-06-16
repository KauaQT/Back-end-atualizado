package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sptech.school.enity.Feedback;
import sptech.school.enity.Usuario;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    // findAllByUsuario
    List<Feedback> findAllByMotorista(Usuario motorista);

    List<Feedback> findByMotorista(Usuario motorista);

    // findByUsuario
    List<Feedback> findByUsuario(Usuario usuario);


    List<Feedback> findByUsuarioOrMotorista(Usuario usuario, Usuario motorista);

}
