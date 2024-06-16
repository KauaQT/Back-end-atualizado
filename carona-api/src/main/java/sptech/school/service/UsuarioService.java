package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sptech.school.dto.FeedbackDetalhes;
import sptech.school.dto.PerfilDto;
import sptech.school.entity.Feedback;
import sptech.school.entity.Usuario;
import sptech.school.repository.FeedbackRepository;
import sptech.school.repository.UsuarioRespository;
import sptech.school.repository.ViagemRepository;
import sptech.school.security.TokenService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRespository usuarioRepository;

    @Autowired
    private TokenService gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;



    public PerfilDto getPerfil(Integer usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            PerfilDto perfilDto = new PerfilDto();
            perfilDto.setNome(usuario.getNome());
            perfilDto.setCpf(usuario.getCpf());
            perfilDto.setEmail(usuario.getEmail());
            perfilDto.setGenero(usuario.getGenero());
            perfilDto.setDataNascimento(usuario.getDataNascimento());
            perfilDto.setCep(usuario.getEndereco().getCep());
            perfilDto.setNumero(usuario.getEndereco().getNumero());
            perfilDto.setTipoUsuario(usuario.getTipoUsuario());
            perfilDto.setQuantidadeViagens(usuario.getViagens().size());

            return perfilDto;
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    private double calcularMediaEstrelas(List<Feedback> feedbacks) {
        if (feedbacks.isEmpty()) {
            return 0.0;
        }

        int totalEstrelas = feedbacks.stream()
                .filter(feedback -> feedback.getCriteriosFeedback() != null)
                .mapToInt(feedback -> feedback.getCriteriosFeedback().getQuantidadeEstrelas())
                .sum();

        return (double) totalEstrelas / feedbacks.size();
    }

    private List<FeedbackDetalhes> calcularDetalhesFeedback(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(feedback -> {
                    FeedbackDetalhes detalhes = new FeedbackDetalhes();
                    detalhes.setNome(feedback.getUsuario().getNome());
                    detalhes.setComentario(feedback.getComentario());
                    detalhes.setQuantidadeEstrelas(feedback.getCriteriosFeedback() != null ?
                            feedback.getCriteriosFeedback().getQuantidadeEstrelas() : null);
                    return detalhes;
                })
                .collect(Collectors.toList());
    }


    public List<Usuario> buscarUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }


    public Usuario buscarUsuarioPorId(int id){
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        return usuario;
    }


}
