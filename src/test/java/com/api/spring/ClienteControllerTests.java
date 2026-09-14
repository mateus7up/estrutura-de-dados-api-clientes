package com.api.spring;

import com.api.spring.repository.ClienteRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ClienteRepository repository;

    @BeforeEach
    void limparBancoDeTeste() {
        repository.deleteAll();
    }

    @Test
    void executaOsCincoEndpointsComTodosOsAtributosDaAula() throws Exception {
        ObjectNode dados = mapper.createObjectNode();
        dados.put("nome", "Cliente Teste");
        dados.put("tipoPessoa", "PF");
        dados.put("cpfCnpj", "11111111112");
        dados.put("telefone", "44999999999");
        dados.put("email", "cliente@example.com");
        dados.put("logradouro", "Avenida Teste");
        dados.put("numero", "S/N");
        dados.put("bairro", "Centro");
        dados.put("cep", "00000000");
        dados.putObject("cidade").put("id", 3040);

        String resposta = mvc.perform(post("/clientes/salvar-cliente")
                        .contentType(MediaType.APPLICATION_JSON).content(dados.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsString();
        long id = mapper.readTree(resposta).get("id").asLong();

        String consulta = mvc.perform(get("/clientes/buscar-cliente/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cidade.id").value(3040))
                .andExpect(jsonPath("$.cidade.nome").value("Paranavaí"))
                .andExpect(jsonPath("$.cidade.estado.uf").value("PR"))
                .andReturn().getResponse().getContentAsString();
        JsonNode salvo = mapper.readTree(consulta);
        assertThat(salvo.size()).isEqualTo(11);
        for (String campo : new String[]{"nome", "tipoPessoa", "cpfCnpj", "telefone",
                "email", "logradouro", "numero", "bairro", "cep"}) {
            assertThat(salvo.get(campo)).as(campo).isEqualTo(dados.get(campo));
        }

        mvc.perform(get("/clientes/listar-clientes"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id));

        dados.put("id", 999999);
        dados.put("nome", "Cliente Atualizado");
        dados.put("tipoPessoa", "PJ");
        dados.put("cpfCnpj", "11111111000111");
        mvc.perform(put("/clientes/atualizar-cliente/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON).content(dados.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Cliente Atualizado"))
                .andExpect(jsonPath("$.tipoPessoa").value("PJ"));
        mvc.perform(get("/clientes/buscar-cliente/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpfCnpj").value("11111111000111"));

        mvc.perform(delete("/clientes/deletar-cliente/{id}", id))
                .andExpect(status().isOk());
        mvc.perform(get("/clientes/listar-clientes"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        assertThat(repository.findById(id)).isEmpty();
    }

    @Test
    void aceitaCadastroSemCidadeComoNoExemploPostmanDaAula() throws Exception {
        mvc.perform(post("/clientes/salvar-cliente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Cliente Sem Cidade\",\"tipoPessoa\":\"PF\","
                                + "\"cpfCnpj\":\"22222222223\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Cliente Sem Cidade"))
                .andExpect(jsonPath("$.cidade").isEmpty());
    }
}
