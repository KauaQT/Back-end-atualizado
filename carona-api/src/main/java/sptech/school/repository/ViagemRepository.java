package sptech.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sptech.school.entity.Usuario;
import sptech.school.entity.Viagem;

import java.time.LocalDate;
import java.util.List;

public interface ViagemRepository extends JpaRepository<Viagem, Integer> {

    // Consulta para buscar todas as viagens juntamente com os motoristas associados filtrando pelo horário
    @Query("SELECT v FROM Viagem v JOIN FETCH v.motorista WHERE v.diaViagem = :horario") // Corrigido para v.diaViagem
    List<Viagem> findViagensAndUsuariosByHorario(LocalDate horario);

    // Método para buscar todas as viagens associadas a um motorista específico
    List<Viagem> findByMotorista(Usuario motorista);

    // Método para contar quantas viagens um motorista realizou com um passageiro específico
    int countByMotoristaAndListaPassageiros(Usuario motorista, Usuario passageiro);

    // findByListaPassageirosContains
    List<Viagem> findByListaPassageirosContains(Usuario passageiro);

}
