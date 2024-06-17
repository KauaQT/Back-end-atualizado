package sptech.school.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import sptech.school.Config.CloudinaryConfig;
import sptech.school.dto.*;
import sptech.school.enity.Endereco;
import sptech.school.enity.Motorista;
import sptech.school.enity.Usuario;
import sptech.school.repository.UsuarioRespository;
import sptech.school.security.TokenService;
import sptech.school.service.EmailService;
import sptech.school.service.GerarCsvService;
import sptech.school.service.UsuarioService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioRespository usuarioRepository;
    private final EmailService emailService;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioRespository usuarioRepository, EmailService emailService, UsuarioMapper usuarioMapper, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.usuarioMapper = usuarioMapper;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<LoginDto> cadastrarUsuario(@RequestBody @Valid UsuarioCriadoDto usuarioDto) {
        final Usuario novoUsuario = new Usuario();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        try {
            novoUsuario.setNome(usuarioDto.getNome());
            novoUsuario.setCpf(usuarioDto.getCpf());
            novoUsuario.setEmail(usuarioDto.getEmail());
            novoUsuario.setDataNascimento(usuarioDto.getDataNascimento());
            novoUsuario.setGenero(usuarioDto.getGenero());
            novoUsuario.setTipoUsuario(usuarioDto.getTipoUsuario());
            novoUsuario.setUrlImagemUsuario(usuarioDto.getUrlImagemUsuario());

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            novoUsuario.setSenha(bCryptPasswordEncoder.encode(usuarioDto.getSenha()));

            Endereco novoEndereco = usuarioDto.getEndereco();
            novoEndereco.setUsuario(novoUsuario);
            novoUsuario.setEndereco(novoEndereco);

            usuarioRepository.save(novoUsuario);

            try {
                emailService.enviarEmail(novoUsuario.getEmail());
            } catch (Exception e) {
                executorService.schedule(() -> {
                    try {
                        emailService.enviarEmail(novoUsuario.getEmail());
                    } catch (Exception ex) {
                        Logger.getLogger("Erro ao enviar email");
                    }
                }, 10, TimeUnit.MINUTES);
            } finally {
                executorService.shutdown();
            }

            LoginDto loginDto = new LoginDto();
            loginDto.setIdUsuario(novoUsuario.getId());
            loginDto.setEmail(novoUsuario.getEmail());
            loginDto.setNome(novoUsuario.getNome());
            loginDto.setCpf(novoUsuario.getCpf());
            loginDto.setGenero(novoUsuario.getGenero());
            loginDto.setDataNascimento(novoUsuario.getDataNascimento().format(DateTimeFormatter.ISO_LOCAL_DATE));
            loginDto.setTipoUsuario(novoUsuario.getTipoUsuario().name());
            loginDto.setUrlImagemUsuario(novoUsuario.getUrlImagemUsuario());
            loginDto.setEndereco(novoUsuario.getEndereco());

            return ResponseEntity.status(HttpStatus.CREATED).body(loginDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PostMapping("/detalhes/{id}")
    public ResponseEntity<PerfilDto> detalhesUsuario(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
          PerfilDto perfil =  usuarioService.getPerfil(id);
            return ResponseEntity.status(200).body(perfil);
        } else {
            return ResponseEntity.status(404).build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (usuario != null && passwordEncoder.matches(senha, usuario.getSenha())) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setIdUsuario(usuario.getId());
            loginResponseDto.setEmail(usuario.getEmail());
            loginResponseDto.setNome(usuario.getNome());
            loginResponseDto.setCpf(usuario.getCpf());
            loginResponseDto.setGenero(usuario.getGenero());
            loginResponseDto.setDataNascimento(usuario.getDataNascimento().format(DateTimeFormatter.ISO_LOCAL_DATE));
            loginResponseDto.setTipoUsuario(usuario.getTipoUsuario().name());
            loginResponseDto.setUrlImagemUsuario(usuario.getUrlImagemUsuario());
            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
        } else {
           // so da que não foi encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/excluir/{id}")
   public ResponseEntity<String> excluirUsuario(@PathVariable int id) {
         Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
       if (optionalUsuario.isPresent()) {
           Usuario usuario = optionalUsuario.get();
           String nomeUsuario = usuario.getNome();
           usuarioRepository.delete(usuario);
              return ResponseEntity.status(200).build();
       } else {
           return ResponseEntity.status(404).build();
       }
   }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioListagemDto>> listarUsuarios() {
        if(usuarioRepository.findAll().isEmpty()){
            return ResponseEntity.status(404).build();
        }
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.status(200).body(UsuarioMapper.toUsuarioListagemDto(usuarios));
    }
    @GetMapping("/listarOrdenado")
    public ResponseEntity<Iterable<Usuario>> listarUsuariosOrdenado() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        quicksort(usuarios, 0, usuarios.size() - 1);
        return ResponseEntity.status(200).body(usuarios);
    }

    private void quicksort(List<Usuario> usuarios, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = particionar(usuarios, inicio, fim);
            quicksort(usuarios, inicio, posicaoPivo - 1);
            quicksort(usuarios, posicaoPivo + 1, fim);
        }
    }

    private int particionar(List<Usuario> usuarios, int inicio, int fim) {
        Usuario pivo = usuarios.get(fim);
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (usuarios.get(j).getNome().compareTo(pivo.getNome()) < 0) {
                i++;
                Usuario temp = usuarios.get(i);
                usuarios.set(i, usuarios.get(j));
                usuarios.set(j, temp);
            }
        }
        Usuario temp = usuarios.get(i + 1);
        usuarios.set(i + 1, usuarios.get(fim));
        usuarios.set(fim, temp);
        return i + 1;
    }

    @PutMapping("/atualizarSenha")
    public ResponseEntity<String> atualizarSenha(@RequestParam String email, @RequestParam String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            usuario.setSenha(senha);
            usuarioRepository.save(usuario);
            return ResponseEntity.status(200).body("Senha atualizada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @PutMapping("/atualizarEmail")
    public ResponseEntity<UsuarioAtualizadoDto> atualizarEmail(@RequestParam String emailAntigo, @RequestParam String emailNovo) {
        Usuario usuario = usuarioRepository.findByEmail(emailAntigo);
        if (usuario != null) {
            usuario.setEmail(emailNovo);
            usuarioRepository.save(usuario);
            return ResponseEntity.status(200).body(UsuarioMapper.toUsuarioAtualizadoDto(usuario));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/solicitarRecuperacaoSenha")
    public ResponseEntity<String> solicitarRecuperacaoSenha(@RequestParam String email) {
        if (!isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            try {
                String codigo = EmailService.enviarTokenRecuperacao(usuario.getEmail());
                usuario.setCodigoRecuperacaoSenha(codigo);
                usuarioRepository.save(usuario);
                return ResponseEntity.status(200).body("Código de recuperação gerado com sucesso");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    @PutMapping("/recuperarSenha")
    public ResponseEntity<UsuarioAtualizadoDto> recuperarSenha(@RequestParam String email, @RequestParam String novaSenha , @RequestParam String token) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            if(validarToken(usuario, token)){
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(novaSenha);
                usuario.setSenha(hashedPassword);
                usuarioRepository.save(usuario);
                return ResponseEntity.status(200).body(UsuarioMapper.toUsuarioAtualizadoDto(usuario));
            }
            return ResponseEntity.status(400).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public static Boolean validarToken(Usuario usuario, String token) {
        return usuario.getCodigoRecuperacaoSenha().equals(token);
    }
    @PutMapping("/editar-motorista")
    public ResponseEntity<UsuarioAtualizadoDto> editarMotorista(@RequestBody Motorista motorista) {
       if(motorista.getTipoUsuario() == Usuario.TipoUsuario.MOTORISTA) {
           motorista.setTipoUsuario(Usuario.TipoUsuario.MOTORISTA);
           usuarioRepository.save(motorista);
              return ResponseEntity.status(200).body(UsuarioMapper.toUsuarioAtualizadoDto(motorista));
       }
        return ResponseEntity.status(400).build();
    }
    @PutMapping("/editar-usuario")
    public ResponseEntity<UsuarioAtualizadoDto> editarUsuario(@RequestBody Usuario usuario) {
        if(usuario.getTipoUsuario() == Usuario.TipoUsuario.PASSAGEIRO) {
            usuario.setTipoUsuario(Usuario.TipoUsuario.PASSAGEIRO);
            usuarioRepository.save(usuario);
            return ResponseEntity.status(200).body(UsuarioMapper.toUsuarioAtualizadoDto(usuario));
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/gerarCsvMotorista/{id}")
    public ResponseEntity<String> gerarCsvMotorista(@PathVariable int id) {

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            GerarCsvService gerarCsvService = new GerarCsvService();
            try {
                List<Usuario> usuarios = new ArrayList<>();
                usuarios.add(usuario.get());
                File file = gerarCsvService.gravaArquivoCsv(usuarios, "motorista");

                EmailService emailService = new EmailService();
                emailService.enviarEmailComAnexo(usuario.get().getEmail(), file);
                return ResponseEntity.status(200).body("Arquivo CSV gerado com sucesso");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Erro ao gerar arquivo CSV");
            }
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }
    @GetMapping("/buscar-nome")
    public ResponseEntity<Usuario> buscarPorNomeBinario(@RequestParam String nome) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        quicksort(usuarios, 0, usuarios.size() - 1);
        Usuario usuario = buscarUsuarioPorNome(usuarios, nome, 0, usuarios.size() - 1);
        if (usuario == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(usuario);
    }

    public Usuario buscarUsuarioPorNome(List<Usuario> usuarios, String nome, int inicio, int fim) {
        if (inicio > fim) {
            return null;
        }

        int meio = (inicio + fim) / 2;
        int comparacao = usuarios.get(meio).getNome().compareTo(nome);

        if (comparacao == 0) {
            return usuarios.get(meio);
        } else if (comparacao > 0) {
            return buscarUsuarioPorNome(usuarios, nome, inicio, meio - 1);
        } else {
            return buscarUsuarioPorNome(usuarios, nome, meio + 1, fim);
        }
    }

    // buscar motoristas
    @GetMapping("/buscar-motoristas")
    public ResponseEntity<List<Usuario>> buscarMotoristas() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Usuario> motoristas = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getTipoUsuario() == Usuario.TipoUsuario.MOTORISTA) {
                motoristas.add(usuario);
            }
        }
        return ResponseEntity.status(200).body(motoristas);
    }
}

