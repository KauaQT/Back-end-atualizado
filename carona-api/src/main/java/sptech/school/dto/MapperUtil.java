package sptech.school.dto;

import org.springframework.stereotype.Component;
import sptech.school.enity.Usuario;

@Component
public class MapperUtil {

    public UsuarioListagemFeedbackDto convertUsuarioToUsuarioListagemFeedbackDto(Usuario usuario) {
        UsuarioListagemFeedbackDto dto = new UsuarioListagemFeedbackDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setGenero(usuario.getGenero());
        return dto;
    }
}
