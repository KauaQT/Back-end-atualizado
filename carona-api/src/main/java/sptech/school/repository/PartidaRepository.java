package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.entity.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Integer> {
}
