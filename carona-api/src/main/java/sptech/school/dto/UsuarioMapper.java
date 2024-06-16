package sptech.school.dto;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import sptech.school.entity.Endereco;
import sptech.school.entity.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Mapper(componentModel = "spring")
public class UsuarioMapper {

    public static UsuarioListagemDto toUsuarioListagemDto(Usuario usuario) {
        UsuarioListagemDto usuarioListagemDto = new UsuarioListagemDto();
        usuarioListagemDto.setNome(usuario.getNome());
        usuarioListagemDto.setEmail(usuario.getEmail());
        usuarioListagemDto.setDataNascimento(usuario.getDataNascimento());
        usuarioListagemDto.setGenero(usuario.getGenero());
        usuarioListagemDto.setTipoUsuario(usuario.getTipoUsuario());
        return usuarioListagemDto;
    }

    // toCadastroDto




    public static UsuarioTokenDto toUsuarioTokenDto(Usuario usuario , String token) {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setSenha(usuario.getSenha());
        usuarioTokenDto.setToken(token);
        return usuarioTokenDto;
    }

    public static List<UsuarioListagemDto> toUsuarioListagemDto(List<Usuario> usuarios) {
        List<UsuarioListagemDto> usuarioListagemDtos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            usuarioListagemDtos.add(toUsuarioListagemDto(usuario));
        }
        return usuarioListagemDtos;
    }

    // toUsuarioAtualizadoDto


    // toUsuarioAtualizacaoSenha
    public static UsuarioAtualizacaoSenha toUsuarioAtualizacaoSenha(Usuario usuario) {
        UsuarioAtualizacaoSenha usuarioAtualizacaoSenha = new UsuarioAtualizacaoSenha();
        usuarioAtualizacaoSenha.setId(usuario.getId());
        usuarioAtualizacaoSenha.setNome(usuario.getNome());
        usuarioAtualizacaoSenha.setCpf(usuario.getCpf());
        usuarioAtualizacaoSenha.setEmail(usuario.getEmail());
        usuarioAtualizacaoSenha.setSenha(usuario.getSenha());
        usuarioAtualizacaoSenha.setGenero(usuario.getGenero());
        usuarioAtualizacaoSenha.setDataNascimento(usuario.getDataNascimento());
        usuarioAtualizacaoSenha.setCodigoRecuperacaoSenha(usuario.getCodigoRecuperacaoSenha());
        return usuarioAtualizacaoSenha;
    }



    public static Usuario toUsuario(UsuarioCriadoDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setGenero(dto.getGenero());
        return usuario;
    }



    // toUsuarioCriadosDto

    public static UsuarioCriadoDto toUsuarioCriadoDto(Usuario usuario) {
        UsuarioCriadoDto usuarioCriadoDto = new UsuarioCriadoDto();
        usuarioCriadoDto.setNome(usuario.getNome());
        usuarioCriadoDto.setCpf(usuario.getCpf());
        usuarioCriadoDto.setEmail(usuario.getEmail());
        usuarioCriadoDto.setDataNascimento(usuario.getDataNascimento());
        usuarioCriadoDto.setGenero(usuario.getGenero());
        return usuarioCriadoDto;
    }

    public static Endereco toEndereco(EnderecoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setUf(dto.getUf());
        endereco.setCidade(dto.getCidade());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        return endereco;
    }

    public static EnderecoDto toEnderecoDto(Endereco endereco) {
        EnderecoDto dto = new EnderecoDto();
        dto.setId(endereco.getId());
        dto.setCep(endereco.getCep());
        dto.setUf(endereco.getUf());
        dto.setCidade(endereco.getCidade());
        dto.setLogradouro(endereco.getLogradouro());
        dto.setBairro(endereco.getBairro());
        return dto;
    }

    public static UsuarioListagemCarro toUsuarioListagemCarro(Usuario usuario) {
        UsuarioListagemCarro usuarioListagemCarro = new UsuarioListagemCarro();
        usuarioListagemCarro.setNome(usuario.getNome());
        usuarioListagemCarro.setCpf(usuario.getCpf());
        usuarioListagemCarro.setEmail(usuario.getEmail());
        usuarioListagemCarro.setDataNascimento(usuario.getDataNascimento());
        usuarioListagemCarro.setCarros(usuario.getCarros().stream()
                .map(CarroMapper::toCarroDto)
                .collect(Collectors.toList()));
        return usuarioListagemCarro;
    }

    public UsuarioCriadosDto toUsuarioCriadosDto(Usuario novoUsuario) {
        UsuarioCriadosDto usuarioCriadosDto = new UsuarioCriadosDto();
        usuarioCriadosDto.setNome(novoUsuario.getNome());
        usuarioCriadosDto.setCpf(novoUsuario.getCpf());
        usuarioCriadosDto.setEmail(novoUsuario.getEmail());
        usuarioCriadosDto.setSenha(novoUsuario.getSenha());
        usuarioCriadosDto.setDataNascimento(novoUsuario.getDataNascimento());
        usuarioCriadosDto.setGenero(novoUsuario.getGenero());
        usuarioCriadosDto.setTipoUsuario(novoUsuario.getTipoUsuario());
        usuarioCriadosDto.setEndereco(novoUsuario.getEndereco());
        return usuarioCriadosDto;
    }

    // toUsuarioAtualizadoDto
    public static UsuarioAtualizadoDto toUsuarioAtualizadoDto(Usuario usuario) {
        UsuarioAtualizadoDto usuarioAtualizadoDto = new UsuarioAtualizadoDto();
        usuarioAtualizadoDto.setNome(usuario.getNome());
        usuarioAtualizadoDto.setCpf(usuario.getCpf());
        usuarioAtualizadoDto.setEmail(usuario.getEmail());
        usuarioAtualizadoDto.setDataNascimento(usuario.getDataNascimento());
        usuarioAtualizadoDto.setGenero(usuario.getGenero());
        return usuarioAtualizadoDto;
    }


}
