package sptech.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.CarroCadastroDto;
import sptech.school.dto.CarroDto;
import sptech.school.dto.UsuarioListagemCarro;
import sptech.school.service.CarroService;

import java.util.List;

@RestController
@RequestMapping("/carro")
public class CarroController {

        private final CarroService carroService;

        @Autowired
        public CarroController(CarroService carroService) {
            this.carroService = carroService;
        }

        @PostMapping("/cadastrar-carro")
        public ResponseEntity<CarroDto> cadastrarCarro(@RequestBody CarroCadastroDto carroCadastroDto) {
            CarroDto carroDto = carroService.cadastrarCarro(carroCadastroDto);
            return ResponseEntity.ok(carroDto);
        }

        @GetMapping("/listar-carros/{idUsuario}")
        public ResponseEntity<List<CarroDto>> listarCarros(@PathVariable Integer idUsuario) {
            List<CarroDto> usuarioListagemCarros = carroService.listarCarros(idUsuario);
            return ResponseEntity.ok(usuarioListagemCarros);
        }
        @PutMapping("/atualizar-carro/{idCarro}")
        public ResponseEntity<CarroDto> atualizarCarro(@PathVariable Integer idCarro, @RequestBody CarroCadastroDto carroCadastroDto) {
            CarroDto carroDto = carroService.atualizarCarro(idCarro, carroCadastroDto);
            return ResponseEntity.ok(carroDto);
        }
        @DeleteMapping("/deletar-carro/{idCarro}")
        public ResponseEntity<Void> deletarCarro(@PathVariable Integer idCarro) {
            carroService.deletarCarro(idCarro);
            return ResponseEntity.noContent().build();
        }
}
