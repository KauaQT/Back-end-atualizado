package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.dto.*;
import sptech.school.enity.Carro;
import sptech.school.enity.Usuario;
import sptech.school.repository.CarroRepository;
import sptech.school.repository.UsuarioRespository;

import java.util.ArrayList;
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

        if(usuario.getTipoUsuario() != Usuario.TipoUsuario.MOTORISTA) {
            throw new IllegalArgumentException("Apenas motoristas podem cadastrar carros");
        }

        Carro carro = CarroMapper.toCarro(carroCadastroDto, usuario);

        Carro savedCarro = carroRepository.save(carro);

        return CarroMapper.toCarroDto(savedCarro);
    }

    public List<ListagemCarro> listarCarros(int usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        List<Carro> carros = carroRepository.findAllByUsuario(usuario);

        List<ListagemCarro> listaCarrosDto = new ArrayList<>();

        for (Carro carro : carros) {
            ListagemCarro listagemCarro = new ListagemCarro();
            listagemCarro.setIdCarro(carro.getId());
            listagemCarro.setMarca(carro.getMarca());
            listagemCarro.setModelo(carro.getModelo());
            listagemCarro.setPlaca(carro.getPlaca());
            listaCarrosDto.add(listagemCarro);
        }

        return listaCarrosDto;
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


