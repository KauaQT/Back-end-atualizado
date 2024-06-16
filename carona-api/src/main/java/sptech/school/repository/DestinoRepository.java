package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.enity.Destino;

import java.util.Optional;

public interface DestinoRepository extends JpaRepository<Destino, Integer> {

    // findByPontoDestino
    Optional findByPontoDestino(String pontoDestino);
}
