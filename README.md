# API de Clientes — atividade de sala de aula

Reprodução dos endpoints de cliente do projeto `estrutura-dados-api` fornecido pelo professor, referência `a04487bb92ff959f15bfae1e6ed163299e2f21e0`.

As classes Java da aplicação e as dependências Maven foram mantidas iguais às do material: entidade, enum, Controller, Service e Repository. A aplicação utiliza **PostgreSQL**, como na aula. Foram acrescentados testes, instruções de execução e uma coleção Postman completa. O projeto-base já utiliza a estrutura do Spring Initializr.

## Executar

Requisitos: **JDK 8 ou JDK 17**, **PostgreSQL** instalado e em execução e internet na primeira execução para baixar o Maven e as dependências. Mantidos Spring Boot **2.7.18**, Java alvo **8**, Spring Web, Spring Data JPA, Lombok e o driver PostgreSQL da aula.

1. No PostgreSQL, crie um banco vazio chamado `concessionaria`. Pelo pgAdmin, use **Databases → Create → Database**. Pelo SQL, execute `CREATE DATABASE concessionaria;` conectado ao banco `postgres`.
2. Conecte-se ao novo banco `concessionaria` e execute os scripts originais `sql/01_pais.sql`, `sql/02_estado.sql` e `sql/03_cidade.sql`, nessa ordem. Eles criam as tabelas e carregam os dados de apoio da aula. Execute-os uma vez, antes da primeira inicialização da aplicação.
3. Abra esta pasta no VS Code (a pasta que contém `pom.xml`) e abra **Terminal → Novo Terminal**.
4. No PowerShell, informe a conexão do seu PostgreSQL e execute:

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5433/concessionaria'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = 'SUA_SENHA_DO_POSTGRESQL'
.\mvnw.cmd spring-boot:run
```

A porta padrão `5433`, o banco `concessionaria` e o usuário `postgres` são os do material da aula. Ajuste `DB_URL` se sua instalação do PostgreSQL usar outra porta. A senha é fornecida pela variável `DB_PASSWORD` e não fica armazenada no repositório.

No mesmo terminal configurado, também é possível executar `.\INICIAR.cmd`. No Linux/macOS, defina as mesmas variáveis de ambiente e use `sh mvnw spring-boot:run`.

Aguarde a mensagem `Started Application`. Abra:

**http://localhost:8080/clientes/listar-clientes**

O resultado inicial `[]` significa que a API está funcionando e ainda não há clientes. Esta atividade é uma API: as respostas são JSON. Para cadastrar, atualizar e excluir, use o Postman conforme explicado abaixo. Para encerrar, pressione **Ctrl+C** no terminal.

Os clientes ficam salvos no PostgreSQL e permanecem cadastrados ao reiniciar a aplicação. As tabelas da entidade Cliente são criadas pelo Hibernate com `ddl-auto=update`, como no material. Os scripts de apoio incluem Paranavaí/PR: cidade `3040`, código IBGE `4118402` e estado `18`.

## Atributos da entidade Cliente

| Atributo | Tipo da aula |
| --- | --- |
| `id` | `Long`, gerado automaticamente |
| `nome` | `String`, não nulo |
| `tipoPessoa` | `TipoPessoa`: `PF` ou `PJ` |
| `cpfCnpj` | `String`, único |
| `telefone` | `String` |
| `email` | `String` |
| `logradouro` | `String` |
| `numero` | `String` |
| `bairro` | `String` |
| `cep` | `String` |
| `cidade` | `Cidade`, relacionamento `@ManyToOne` |

As entidades `Cidade` e `Estado` e os endpoints auxiliares de cidade também foram preservados. Não foram acrescentados campos à entidade Cliente.

## Os cinco endpoints da aula

| Método | Caminho | Operação |
| --- | --- | --- |
| POST | `/clientes/salvar-cliente` | Cadastrar |
| GET | `/clientes/listar-clientes` | Listar |
| GET | `/clientes/buscar-cliente/{id}` | Buscar pelo ID |
| PUT | `/clientes/atualizar-cliente/{id}` | Atualizar |
| DELETE | `/clientes/deletar-cliente/{id}` | Excluir |

A URL base é `http://localhost:8080`. Os retornos de sucesso mantêm o comportamento original: HTTP 200; a exclusão retorna corpo vazio. Na atualização, envie todos os campos, pois o Service copia as propriedades recebidas, preservando o ID da URL. As validações e o tratamento de erros também permanecem como no exemplo da aula.

Exemplo de JSON para salvar ou atualizar, com `Content-Type: application/json`:

```json
{
  "nome": "Cliente Teste",
  "tipoPessoa": "PF",
  "cpfCnpj": "11111111112",
  "telefone": "44999999999",
  "email": "cliente@example.com",
  "logradouro": "Avenida Teste",
  "numero": "S/N",
  "bairro": "Centro",
  "cep": "00000000",
  "cidade": { "id": 3040 }
}
```

Use dados fictícios. O ID retornado no cadastro deve ser utilizado nas demais operações. Também é possível omitir `cidade`, como no exemplo Postman original. Ao informá-la, use o ID de uma cidade já cadastrada. Não repita um CPF/CNPJ existente, pois a coluna é única.

## Testar no Postman

Importe `postman/clientes.postman_collection.json`. Com a aplicação executando, rode as cinco primeiras requisições na ordem indicada. O cadastro guarda automaticamente o ID na variável `clienteId`, usada pela busca, atualização e exclusão. A coleção também contém as duas consultas de cidade do material.

Para conferir a listagem após excluir, execute novamente **02 — Listar clientes**. As requisições usam os caminhos exatos do Controller da aula.

## Testes automatizados e JAR

Crie também um banco PostgreSQL exclusivo chamado `concessionaria_testes`, executando `CREATE DATABASE concessionaria_testes;` conectado ao banco `postgres`. Esse banco é destinado somente aos testes, que recriam as tabelas mapeadas e carregam registros de apoio. Mantenha `DB_USERNAME` e `DB_PASSWORD` configurados no terminal e execute:

```powershell
$env:TEST_DB_URL = 'jdbc:postgresql://localhost:5433/concessionaria_testes'
.\mvnw.cmd clean verify
java -jar target\unipar-0.0.1-SNAPSHOT.jar
```

Os testes usam PostgreSQL e verificam a inicialização, os cinco endpoints com todos os atributos, o relacionamento com cidade, os valores PF/PJ, a preservação do ID na atualização, a exclusão e o cadastro sem cidade. `TEST_DB_URL` deve apontar para o banco exclusivo de testes. O JAR usa a conexão `DB_URL` do banco `concessionaria`.

## Publicar e entregar

Publique o conteúdo desta pasta em um repositório público do seu GitHub, com `pom.xml`, `mvnw`, `mvnw.cmd`, `.mvn/`, `src/`, `postman/`, `sql/` e este README. A pasta `target/` e arquivos de senha não devem ser publicados. Envie os arquivos descompactados para que o código fique disponível para consulta.

Confirme que o repositório abre sem login e envie **o link do repositório** na atividade. A aplicação deve continuar executável pelos comandos acima.

Referências técnicas: [Spring Initializr](https://start.spring.io/) e [configuração de bancos no Spring Boot 2.7.18](https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/data.html#data.sql.datasource.embedded).
