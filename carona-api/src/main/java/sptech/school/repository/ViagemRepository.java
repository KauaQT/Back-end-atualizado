package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sptech.school.enity.Usuario;
import sptech.school.enity.Viagem;

import java.time.LocalDate;
import java.util.List;

public interface ViagemRepository extends JpaRepository<Viagem, Integer> {

    // Consulta para buscar todas as viagens juntamente com os motoristas associados filtrando pelo horário

    // Método para buscar todas as viagens associadas a um motorista específico
    List<Viagem> findByMotorista(Usuario motorista);

    // Método para contar quantas viagens um motorista realizou com um passageiro específico
    int countByMotoristaAndListaPassageiros(Usuario motorista, Usuario passageiro);

    // findByListaPassageirosContains
    List<Viagem> findByListaPassageirosContains(Usuario passageiro);

    @Query("SELECT v FROM Viagem v WHERE v.diaViagem = :dataViagem")
    List<Viagem> findViagensByDataViagem(@Param("dataViagem") LocalDate dataViagem);

}
