package sptech.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransacaoRespostaQrCodeDto {
    @JsonProperty("point_of_interaction")
    private PaymentPointOfInteraction pointOfInteraction;
    private String status;

    @Data
    public static class PaymentPointOfInteraction {
        @JsonProperty("transaction_data")
        private TransactionData transactionData;

        public static class TransactionData {
            @JsonProperty("ticket_url")
            private String ticketUrl;
        }
    }
}

