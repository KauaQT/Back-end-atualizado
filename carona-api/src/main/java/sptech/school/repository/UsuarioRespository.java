package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sptech.school.enity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRespository extends JpaRepository<Usuario, Integer> {

    Usuario findByEmailAndSenha(String email, String senha);

    Usuario findByEmail(String email);

    List<Usuario> findByNome(String nome);

    Optional<Usuario> findById(Integer id);

    // findByUsuario

    List<Usuario> findByTipoUsuario(Usuario tipoUsuario);
}
