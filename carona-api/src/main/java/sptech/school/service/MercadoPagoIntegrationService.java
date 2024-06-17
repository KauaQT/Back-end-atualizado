package sptech.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sptech.school.dto.TransacaoRequisicaoDto;
import sptech.school.dto.TransacaoRespostaQrCodeDto;

import java.util.UUID;

@Service
public class MercadoPagoIntegrationService {

    @Autowired
    private MercadoPagoClient mercadoPagoClient;

    private String token = "Bearer APP_USR-7256193891785388-053019-3b70628d81e1d8cb895dee46fd83d92a-1808777716";

    public TransacaoRespostaQrCodeDto gerarQrCode(TransacaoRequisicaoDto dto) {
        return mercadoPagoClient.gerarQrCode(dto, token, UUID.randomUUID().toString()).getBody();
    }
}
