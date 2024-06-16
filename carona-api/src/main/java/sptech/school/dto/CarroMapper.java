package sptech.school.dto;
import sptech.school.enity.Carro;
import sptech.school.enity.Usuario;

public class CarroMapper {
    public static CarroDto toCarroDto(Carro carro) {
        CarroDto carroDto = new CarroDto();
        carroDto.setCor(carro.getCor());
        carroDto.setMarca(carro.getMarca());
        carroDto.setModelo(carro.getModelo());
        carroDto.setPlaca(carro.getPlaca());
        return carroDto;
    }
    public static Carro toCarro(CarroDto carroDto) {
        Carro carro = new Carro();
        carro.setCor(carroDto.getCor());
        carro.setMarca(carroDto.getMarca());
        carro.setModelo(carroDto.getModelo());
        carro.setPlaca(carroDto.getPlaca());
        return carro;
    }

    public static Carro toCarro(CarroCadastroDto carroCadastroDto, Usuario usuario) {
        Carro carro = new Carro();
        carro.setCor(carroCadastroDto.getCor());
        carro.setMarca(carroCadastroDto.getMarca());
        carro.setModelo(carroCadastroDto.getModelo());
        carro.setPlaca(carroCadastroDto.getPlaca());
        carro.setUsuario(usuario);
        return carro;
    }

    //updateCarroFromDto
    public static void updateCarroFromDto(Carro carro, CarroCadastroDto carroCadastroDto) {
        carro.setCor(carroCadastroDto.getCor());
        carro.setMarca(carroCadastroDto.getMarca());
        carro.setModelo(carroCadastroDto.getModelo());
        carro.setPlaca(carroCadastroDto.getPlaca());
    }
}

