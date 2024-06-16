
package sptech.school.service;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sptech.school.enity.Endereco;

@Service
public class CepService {
    private final RestTemplate restTemplate;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    public Endereco buscarEnderecoPorCep(String cep) {
//        String url = "https://viacep.com.br/ws/" + cep + "/json/";
//
//        Endereco endereco = restTemplate.getForObject(url, Endereco.class);
//
//        if (endereco == null) {
//            return null;
//        } else {
//            Endereco enderecoUsuario = new Endereco();
//            enderecoUsuario.setCep(endereco.getCep());
//            enderecoUsuario.setLogradouro(endereco.getLogradouro());
//            enderecoUsuario.setBairro(endereco.getBairro());
//            enderecoUsuario.setCidade(endereco.getCidade());
//            enderecoUsuario.setUF(endereco.getUF());
//            return enderecoUsuario;
//        }
//    }
}
