package sptech.school.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sptech.school.enity.Carro;
import sptech.school.enity.Endereco;
import sptech.school.enity.Viagem;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    private String cpf;

    private String email;

    private String senha;

    private LocalDate dataNascimento;

    private String genero;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;


    public enum TipoUsuario {
        MOTORISTA,
        PASSAGEIRO
    }

    private String urlImagemUsuario; // Novo campo para a URL da imagem

    private String codigoRecuperacaoSenha;

    @ElementCollection
    private List<String> destinosDisponiveis;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private Endereco endereco;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    @JsonManagedReference
    private List<Carro> carros = new ArrayList<>();

    @OneToMany(mappedBy = "motorista")
    private List<Viagem> viagens;


    @OneToMany(mappedBy = "usuario")
    private List<Feedback> feedbacks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getIdade() {
        if (dataNascimento != null) {
            LocalDate today = LocalDate.now();
            return Period.between(dataNascimento, today).getYears();
        }
        return null;
    }


}
