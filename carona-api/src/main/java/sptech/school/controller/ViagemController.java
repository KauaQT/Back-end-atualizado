package sptech.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.*;
import sptech.school.entity.Usuario;
import sptech.school.entity.Viagem;
import sptech.school.service.UsuarioService;
import sptech.school.service.ViagemService;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

    private final ViagemService viagemService;

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    public ViagemController(ViagemService viagemService , UsuarioService usuarioService) {
        this.viagemService = viagemService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/reservar-viagem/{idViagem}/{idUsuario}")
    public ResponseEntity<ReservaViagemDto> reservarViagem(@PathVariable Integer idViagem, @PathVariable Integer idUsuario) {
        try {
            ReservaViagemDto reservaViagemDto = viagemService.reservarViagem(idViagem, idUsuario);
            return ResponseEntity.ok(reservaViagemDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/finalizar-viagem/{idViagem}/{idMotorista}")
    public ResponseEntity<Void> finalizarViagem(@PathVariable Integer idViagem, @PathVariable Integer idMotorista) {
        try {
            viagemService.finalizarViagem(idViagem, idMotorista);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/criar-viagem")
    public ResponseEntity<RetornoViagemDto> createViagem(@RequestBody ViagemDTO viagemDTO) {
        if (viagemDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            RetornoViagemDto retornoViagemDto = viagemService.saveViagem(viagemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(retornoViagemDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/buscar-viagens")
    public ResponseEntity<List<ListagemMotoristaViagem>> buscarMotoristasProximos(@RequestBody BuscarViagemDto dto) {
        try {

            List<ListagemMotoristaViagem> motoristaProximosDto = viagemService.buscarViagensProximas(dto.getLatitudePartida() ,
                    dto.getLongitudeDestino() , dto.getLatitudeDestino() , dto.getLongitudeDestino(), dto.getDiaViagem());
            return ResponseEntity.ok(motoristaProximosDto);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.error("Erro ao buscar motoristas próximos:", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/viagensFidelizadas/{idMotorista}")
    public ResponseEntity<List<PassageirosFidelizadosDto>> buscarPassageirosFidelizados(@PathVariable Integer idMotorista) {
        Usuario motorista = usuarioService.buscarUsuarioPorId(idMotorista);
        if (motorista == null) {
            throw new IllegalArgumentException("Motorista não encontrado com o ID fornecido");
        }

        List<Viagem> viagensMotorista = viagemService.listarViagensPorMotorista(motorista);

        Map<Integer, Integer> viagensPorPassageiro = new HashMap<>();
        for (Viagem viagem : viagensMotorista) {
            List<Usuario> passageiros = viagem.getListaPassageiros();
            for (Usuario passageiro : passageiros) {
                int quantidadeAtual = viagensPorPassageiro.getOrDefault(passageiro.getId(), 0);
                viagensPorPassageiro.put(passageiro.getId(), quantidadeAtual + 1);
            }
        }

        List<PassageirosFidelizadosDto> passageirosFidelizados = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : viagensPorPassageiro.entrySet()) {
            if (entry.getValue() >= 5) {
                Usuario passageiro = usuarioService.buscarUsuarioPorId(entry.getKey());
                if (passageiro != null) {
                    PassageirosFidelizadosDto dto = new PassageirosFidelizadosDto();
                    dto.setNomePassageiro(passageiro.getNome());
                    dto.setQuantidadeViagensRealizadas(entry.getValue());

                    LocalDate dataNascimento = passageiro.getDataNascimento();
                    if (dataNascimento != null) {
                        LocalDate hoje = LocalDate.now();
                        int idade = Period.between(dataNascimento, hoje).getYears();
                        dto.setIdade(idade);
                    } else {
                        dto.setIdade(null);
                    }
                    passageirosFidelizados.add(dto);
                }
            }
        }

        return ResponseEntity.ok(passageirosFidelizados);
    }


    @GetMapping("/listar-viagens/{id}")
    public ResponseEntity<List<ListaViagensDto>> listarViagens(@PathVariable Integer id) {
        Usuario motorista = usuarioService.buscarUsuarioPorId(id);
        if (motorista == null) {
            throw new IllegalArgumentException("Motorista não encontrado com o ID fornecido");
        }

        List<Viagem> viagensMotorista = viagemService.listarViagensPorMotorista(motorista);

        List<ViagensDto> viagensMotoristaDtoList = new ArrayList<>();
        for (Viagem viagem : viagensMotorista) {
            ViagensDto viagensDto = new ViagensDto();
            viagensDto.setOrigem(viagem.getPartida().getPontoPartida());
            viagensDto.setDestino(viagem.getDestino().getPontoDestino());
            viagensDto.setValor(viagem.getValor());
            viagensDto.setDataViagem(viagem.getDiaViagem());
            viagensDto.setStatus(viagem.getStatus());
            viagensDto.setIdViagem(viagem.getIdViagem());
            viagensMotoristaDtoList.add(viagensDto);

        }

        ListaViagensDto listaViagensDto = new ListaViagensDto();
        listaViagensDto.setViagens(viagensMotoristaDtoList);

        List<ListaViagensDto> listaListasViagensDto = new ArrayList<>();
        listaListasViagensDto.add(listaViagensDto);

        return ResponseEntity.ok(listaListasViagensDto);
    }

    @GetMapping("/listar-viagens-passageiro/{id}")
    public ResponseEntity<List<ListaViagensDto>> listarViagensPassageiro(@PathVariable Integer id) {
        Usuario passageiro = usuarioService.buscarUsuarioPorId(id);
        if (passageiro == null) {
            throw new IllegalArgumentException("Passageiro não encontrado com o ID fornecido");
        }

        List<Viagem> viagensPassageiro = viagemService.listarViagensPorPassageiro(passageiro);

        List<ViagensDto> viagensPassageiroDtoList = new ArrayList<>();
        for (Viagem viagem : viagensPassageiro) {
            ViagensDto viagensDto = new ViagensDto();
            viagensDto.setOrigem(viagem.getPartida().getPontoPartida());
            viagensDto.setDestino(viagem.getDestino().getPontoDestino());
            viagensDto.setValor(viagem.getValor());
            viagensDto.setDataViagem(viagem.getDiaViagem());
            viagensDto.setStatus(viagem.getStatus());
            viagensDto.setIdViagem(viagem.getIdViagem());
            viagensPassageiroDtoList.add(viagensDto);
        }

        ListaViagensDto listaViagensDto = new ListaViagensDto();
        listaViagensDto.setViagens(viagensPassageiroDtoList);

        List<ListaViagensDto> listaListasViagensDto = new ArrayList<>();
        listaListasViagensDto.add(listaViagensDto);

        return ResponseEntity.ok(listaListasViagensDto);
    }

    @GetMapping("/detalhesViagens/{idViagens}")
    public ResponseEntity<DetalhesViagemDto> detalhesViagens(@PathVariable Integer idViagens) {
     return null;
    }
}
