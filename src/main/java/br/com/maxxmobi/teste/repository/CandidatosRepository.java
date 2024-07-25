package br.com.maxxmobi.teste.repository;

import br.com.maxxmobi.teste.model.CandidadosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface CandidatosRepository extends JpaRepository<CandidadosModel, Long> {

    // Pesquisa por nome, ignorando maiúsculas e minúsculas
    List<CandidadosModel> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);

    // Pesquisa por data de nascimento
    List<CandidadosModel> findAllByNascimento(@Param("nascimento") Date nascimento);


    // Pesquisa por sexo, ignorando maiúsculas e minúsculas
    List<CandidadosModel> findAllBySexoContainingIgnoreCase(@Param("sexo") String sexo);

    // Pesquisa por nota
    List<CandidadosModel> findAllByNota(@Param("nota") Integer nota);
}
