
package sptech.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.school.dto.TransacaoRequisicaoDto;
import sptech.school.dto.TransacaoRespostaQrCodeDto;
import sptech.school.service.MercadoPagoIntegrationService;


@RestController
@RequestMapping("/pagamento")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoIntegrationService mercadoPagoIntegrationService;

    @PostMapping
    @CrossOrigin
    public ResponseEntity<TransacaoRespostaQrCodeDto> sendPayment(@RequestBody TransacaoRequisicaoDto messageDto){
        TransacaoRespostaQrCodeDto resposta = mercadoPagoIntegrationService.gerarQrCode(messageDto);
        return ResponseEntity.ok(resposta);
    }

}
