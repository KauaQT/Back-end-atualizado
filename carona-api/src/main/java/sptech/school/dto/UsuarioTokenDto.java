package sptech.school.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioTokenDto {

    private String email;

    private String senha;

    private String token;

}
