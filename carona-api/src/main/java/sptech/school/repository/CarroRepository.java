package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.dto.ListagemCarro;
import sptech.school.enity.Carro;
import sptech.school.enity.Usuario;

import java.util.List;
import java.util.Optional;

public interface CarroRepository extends JpaRepository<Carro, Integer> {

    List<Carro> findAllByUsuario(Usuario usuario);

    // findByModeloAndPlaca
    Optional<Carro> findByModeloAndPlaca(String modelo, String placa);

    //
}