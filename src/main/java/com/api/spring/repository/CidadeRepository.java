package com.api.spring.repository;

import com.api.spring.entidades.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    Optional<Cidade> findByIbge(Integer ibge);


    List<Cidade> findFirst10ByNomeContainingIgnoreCase(String nome);

//   MÉTODO NATIVO PARA LISTAR TODAS AS CIDADES
//    @Query(value = "SELECT * FROM cidade", nativeQuery = true)
//    List<Cidade> listarCidades();
}
