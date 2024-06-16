package sptech.school.controller;

import com.itextpdf.text.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.FeedbackEspecificoDto;
import sptech.school.dto.FeedbackPrincipalDto;
import sptech.school.dto.FeedbackRequestDto;
import sptech.school.dto.RetornoFeedbackDto;
import sptech.school.enity.CriteriosFeedback;
import sptech.school.enity.Feedback;
import sptech.school.enity.Usuario;
import sptech.school.service.FeedBackService;
import sptech.school.service.UsuarioService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {

    @Autowired
    private FeedBackService feedbackService;


    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/criar-feedback")
    public ResponseEntity<RetornoFeedbackDto> criarFeedback(@RequestBody FeedbackRequestDto feedbackRequest) {
        // Criar e preencher o objeto CriteriosFeedback
        CriteriosFeedback criteriosFeedback = new CriteriosFeedback();
        criteriosFeedback.setQuantidadeEstrelas(feedbackRequest.getPontualidade()); // Pode ajustar para corresponder aos campos reais
        criteriosFeedback.setQuantidadePontualidade(feedbackRequest.getPontualidade());
        criteriosFeedback.setQuantidadeComunicacao(feedbackRequest.getComunicacao());
        criteriosFeedback.setQuantidadeDirigibilidade(feedbackRequest.getDirigibilidade());
        criteriosFeedback.setQuantidadeSeguranca(feedbackRequest.getSeguranca());

        // Criar e preencher o objeto Feedback
        Feedback feedback = new Feedback();
        feedback.setComentario(feedbackRequest.getComentario());

        Usuario usuario = usuarioService.buscarUsuarioPorId(feedbackRequest.getIdUsuario());
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID fornecido");
        }

        // Salvar o feedback associado ao usuário, motorista e aos critérios de feedback
        Feedback savedFeedback = feedbackService.saveFeedbackWithCriteriaAndUsers(
                feedback, criteriosFeedback, usuario, feedbackRequest.getIdMotorista());

        // Converter o Feedback salvo para RetornoFeedbackDto, se necessário
        RetornoFeedbackDto retornoFeedbackDto = feedbackService.mapToRetornoFeedbackDto(savedFeedback);

        return ResponseEntity.ok(retornoFeedbackDto);
    }

    // feedback do usuario que deu pras os motoristas


    @GetMapping("/listar-feedbacks-motorista/{idMotorista}")
    public ResponseEntity<List<FeedbackPrincipalDto>> listarFeedbacks(@PathVariable Integer idMotorista) {
        Usuario motorista = usuarioService.buscarUsuarioPorId(idMotorista);
        if (motorista == null) {
            throw new IllegalArgumentException("Motorista não encontrado com o ID fornecido");
        }

        List<FeedbackPrincipalDto> feedbacksDto = feedbackService.listarFeedbacksPrincipalPorMotorista(motorista);

        return ResponseEntity.ok(feedbacksDto);
    }
}
