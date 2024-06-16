package sptech.school.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sptech.school.entity.Usuario;
import sptech.school.repository.UsuarioRespository;

@Service
public class
AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRespository usuarioRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRespository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Dados inválidos");
        }
        return (UserDetails) usuario;
    }
}