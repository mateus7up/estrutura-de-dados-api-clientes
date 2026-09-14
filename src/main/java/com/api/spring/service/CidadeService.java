package com.api.spring.service;

import com.api.spring.entidades.Cidade;
import com.api.spring.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    public Cidade buscarPorCodigoIbge(Integer codigoIbge) {
        Cidade cidade = cidadeRepository.findByIbge(codigoIbge).orElseThrow(() -> new RuntimeException("Cidade não encontrada!"));
        return cidade;
    }

    public List<Cidade> buscarCidadesPorNome(String nome) {
        List<Cidade> cidadesListadas = cidadeRepository.findFirst10ByNomeContainingIgnoreCase(nome);
        return cidadesListadas;
    }
}
