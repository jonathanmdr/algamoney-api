# AlgaMoneyAPI
Api RESTful desenvolvida em Java com Spring Boot.

[![node](https://img.shields.io/badge/Java-1.8.0-lightgray.svg)](https://www.java.com/pt_BR/download/)
[![node](https://img.shields.io/badge/Maven-3.5.4-steelblue.svg)](https://maven.apache.org/download.cgi)
[![node](https://img.shields.io/badge/Plugin-Lombok_1.18.4-indianRed.svg)](https://projectlombok.org/)
[![node](https://img.shields.io/badge/SpringBoot-1.5.3--RELEASE-green.svg)](http://spring.io/projects/spring-boot)
[![node](https://img.shields.io/badge/Database-PostgreSQL--11.2-blue.svg)](https://www.postgresql.org/download/)
[![node](https://img.shields.io/badge/Security-Oauth--2-black.svg)](https://oauth.net/2/)
[![node](https://img.shields.io/badge/Security-JWT-purple.svg)](https://jwt.io/)

### O que é o AlgaMoneyAPI?
Esta API foi desenvolvida para gerir um controle simples de ganhos x gastos, possibilitando salvar estes registros e movimentá-los.

### Funcionalidades: 
- Cadastro de categorias
- Cadastro de pessoas
- Lançamentos (Receita x Despesa)

As funcionalidades acima possuem o CRUD completo, o que possibilita o usuário movimentar com facilidade todas as operações de cadastro, alteração, consulta e exclusão de registros.

### Segurança da API:
- Cadastro de usuário
- Cadastro de permissões de acesso
- Cadastro de permissões de acesso x usuário
- API habilitada para funcionar com HTTPS
- Todas as requisições são movimentadas com accessToken e refreshToken gerenciado pelo OAuth2 com JWT

### Tecnologias utilizadas no projeto:
 - Java 8
 - Spring Boot
 - Spring Data JPA
 - Spring Security
 - OAuth2 Security
 - JWT
 - Apache Commons
 - Lombok
 - Jackson
 - Flyway
 - Maven
 - PostgreSQL
 
 A API está disponível no Heroku através da URL: https://algamoney-api-jhm.herokuapp.com/
