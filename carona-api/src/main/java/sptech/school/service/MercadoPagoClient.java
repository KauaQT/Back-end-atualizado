package sptech.school.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import sptech.school.dto.TransacaoRequisicaoDto;
import sptech.school.dto.TransacaoRespostaQrCodeDto;

@FeignClient(name = "mercado-pago", url = "https://api.mercadopago.com/v1")
public interface MercadoPagoClient {

    @PostMapping("/payments")
    public ResponseEntity<TransacaoRespostaQrCodeDto> gerarQrCode(
            @RequestBody TransacaoRequisicaoDto dto,
            @RequestHeader("Authorization") String token,
            @RequestHeader("X-Idempotency-Key") String identification);
}
