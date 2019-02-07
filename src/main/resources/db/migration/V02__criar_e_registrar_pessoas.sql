CREATE TABLE pessoa (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL,
  logradouro VARCHAR(30),
  numero VARCHAR(30),
  complemento VARCHAR(30),
  bairro VARCHAR(30),
  cep VARCHAR(30),
  cidade VARCHAR(30),
  estado VARCHAR(30),
  ativo BOOLEAN NOT NULL
);

INSERT INTO pessoa(nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo)
VALUES ('Jonathan Henrique Medeiros', 'Rua Anibal Bonato', '292', 'Ap 01', 'São Francisco Xavier', '85.660-000', 'Dois Vizinhos', 'PR', TRUE),
       ('Jonathan Angeli', 'Rua Anibal Bonato', '292', 'Ap 01', 'São Francisco Xavier', '85.660-000', 'Dois Vizinhos', 'PR', TRUE),
       ('Alcides Medeiros', 'Av. Ascânio M. de Carvalho', '1091', 'Casa Fundos', 'Centro', '85.4400-000', 'Ubiratã', 'PR', TRUE),
       ('Margarida Medeiros', 'Av. Ascânio M. de Carvalho', '1091', 'Casa Fundos', 'Centro', '85.440-000', 'Ubiratã', 'PR', TRUE),
       ('Guilherme Medeiros', 'Av. Ascânio M. de Carvalho', '1091', 'Casa Fundos', 'Centro', '85.440-000', 'Ubiratã', 'PR', TRUE);