package sptech.school.dto;

import sptech.school.enity.Endereco;

public class EnderecoMapper {

    public static EnderecoListagemDto toEnderecoListagemDto(Endereco endereco) {
        EnderecoListagemDto enderecoListagemDto = new EnderecoListagemDto();
        enderecoListagemDto.setId(endereco.getId());
        enderecoListagemDto.setCep(endereco.getCep());
        enderecoListagemDto.setLogradouro(endereco.getLogradouro());
        enderecoListagemDto.setNumero(endereco.getNumero());
        enderecoListagemDto.setBairro(endereco.getBairro());
        enderecoListagemDto.setCidade(endereco.getCidade());
        return enderecoListagemDto;
    }
}
