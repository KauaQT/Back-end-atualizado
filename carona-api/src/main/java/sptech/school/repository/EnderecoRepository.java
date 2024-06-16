package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.enity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}
