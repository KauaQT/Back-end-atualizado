package sptech.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransacaoRequisicaoDto {
    @JsonProperty("transaction_amount")
    private Double transactionAmount;
    private String description;
    @JsonProperty("payment_method_id")
    private String paymentMethodId;
    private int installments;
    private Payer payer;


    @Data
    public static class Payer {
        private String email;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private Indentification identification;
    }

    @Data
    public static class Indentification {
        private String type;
        private String number;
    }
}
