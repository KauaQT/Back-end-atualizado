package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.dto.*;
import sptech.school.enity.CriteriosFeedback;
import sptech.school.enity.Feedback;
import sptech.school.enity.Usuario;
import sptech.school.repository.CriteriosFeedbackRepository;
import sptech.school.repository.FeedbackRepository;
import sptech.school.repository.UsuarioRespository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedBackService {

    @Autowired
    private CriteriosFeedbackRepository criteriosFeedbackRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UsuarioRespository usuarioRepository;
    @Autowired
    private MapperUtil mapperUtil;


    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public CriteriosFeedback saveCriteriosFeedback(CriteriosFeedback criteriosFeedback) {
        return criteriosFeedbackRepository.save(criteriosFeedback);
    }

    public Feedback saveFeedbackWithCriteriaAndUsers(Feedback feedback, CriteriosFeedback criteriosFeedback, Usuario usuario, int motoristaId) {
        // Buscar o motorista pelo ID
        Usuario motorista = usuarioRepository.findById(motoristaId)
                .orElseThrow(() -> new IllegalArgumentException("Motorista não encontrado com o ID fornecido"));

        feedback.setUsuario(usuario);
        feedback.setMotorista(motorista);
        feedback.setCriteriosFeedback(criteriosFeedback);

        return feedbackRepository.save(feedback);
    }


    public List<Feedback> listarFeedbacksPorMotorista(Usuario motorista) {
        return feedbackRepository.findAllByMotorista(motorista);
    }

    public List<FeedbackPrincipalDto> listarFeedbacksPrincipalPorMotorista(Usuario motorista) {
        List<Feedback> feedbacks = listarFeedbacksPorMotorista(motorista);
        return feedbacks.stream()
                .map(FeedbackPrincipalDto::new)
                .collect(Collectors.toList());
    }


    public RetornoFeedbackDto mapToRetornoFeedbackDto(Feedback feedback) {
        RetornoFeedbackDto dto = new RetornoFeedbackDto();
        dto.setComentario(feedback.getComentario());
        dto.setUsuario(mapperUtil.convertUsuarioToUsuarioListagemFeedbackDto(feedback.getUsuario()));
        dto.setMotorista(mapperUtil.convertUsuarioToUsuarioListagemFeedbackDto(feedback.getMotorista()));
        return dto;
    }

    public FeedbackEspecificoDto getFeedbackEspecificoDtoById(Integer idFeedback) {
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(idFeedback);
        if (feedbackOptional.isPresent()) {
            Feedback feedback = feedbackOptional.get();
            return mapToFeedbackEspecificoDto(feedback);
        } else {
            throw new IllegalArgumentException("Feedback não encontrado com o ID fornecido");
        }
    }

    private FeedbackEspecificoDto mapToFeedbackEspecificoDto(Feedback feedback) {
        FeedbackEspecificoDto dto = new FeedbackEspecificoDto();
        dto.setComentario(feedback.getComentario());
        dto.setQuantidadeEstrelasPontualidade(feedback.getCriteriosFeedback().getQuantidadePontualidade());
        dto.setQuantidadeEstrelasComunicacao(feedback.getCriteriosFeedback().getQuantidadeComunicacao());
        dto.setQuantidadeEstrelasDirigibilidade(feedback.getCriteriosFeedback().getQuantidadeDirigibilidade());
        dto.setQuantidadeEstrelasSeguranca(feedback.getCriteriosFeedback().getQuantidadeSeguranca());
        return dto;
    }
}
