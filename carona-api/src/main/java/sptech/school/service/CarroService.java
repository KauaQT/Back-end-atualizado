package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.dto.CarroCadastroDto;
import sptech.school.dto.CarroDto;
import sptech.school.dto.CarroMapper;
import sptech.school.dto.UsuarioListagemCarro;
import sptech.school.entity.Carro;
import sptech.school.entity.Usuario;
import sptech.school.repository.CarroRepository;
import sptech.school.repository.UsuarioRespository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private UsuarioRespository usuarioRepository;

    public CarroDto cadastrarCarro(CarroCadastroDto carroCadastroDto) {
        Usuario usuario = usuarioRepository.findById(carroCadastroDto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Carro carro = CarroMapper.toCarro(carroCadastroDto, usuario);
        Carro savedCarro = carroRepository.save(carro);
        return CarroMapper.toCarroDto(savedCarro);
    }
    public List<UsuarioListagemCarro> listarCarrosDoUsuario(int usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        List<Carro> carros = carroRepository.findAllByUsuario(usuario);
        List<CarroDto> carrosDto = carros.stream()
                .map(CarroMapper::toCarroDto)
                .collect(Collectors.toList());

        UsuarioListagemCarro usuarioListagemCarro = new UsuarioListagemCarro();
        usuarioListagemCarro.setNome(usuario.getNome());
        usuarioListagemCarro.setCpf(usuario.getCpf());
        usuarioListagemCarro.setEmail(usuario.getEmail());
        usuarioListagemCarro.setDataNascimento(usuario.getDataNascimento());
        usuarioListagemCarro.setCarros(carrosDto);

        return Collections.singletonList(usuarioListagemCarro);
    }

    // listarCarros
    public List<CarroDto> listarCarros(int usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        List<Carro> carros = carroRepository.findAllByUsuario(usuario);
        return carros.stream()
                .map(CarroMapper::toCarroDto)
                .collect(Collectors.toList());
    }
    public CarroDto atualizarCarro(int carroId, CarroCadastroDto carroCadastroDto) {
        Carro carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new IllegalArgumentException("Carro não encontrado"));
        CarroMapper.updateCarroFromDto(carro, carroCadastroDto);
        Carro savedCarro = carroRepository.save(carro);
        return CarroMapper.toCarroDto(savedCarro);
    }

    // deletarCarro
    public void deletarCarro(int carroId) {
        Carro carro = carroRepository.findById(carroId)
                .orElseThrow(() -> new IllegalArgumentException("Carro não encontrado"));
        carroRepository.delete(carro);
    }
}


