CREATE TABLE contato (
    id BIGSERIAL PRIMARY KEY,
    idpessoa BIGINT NOT NULL,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    FOREIGN KEY(idpessoa) REFERENCES pessoa(id)
);

INSERT INTO contato(idpessoa, nome, email, telefone) VALUES(1, 'Jonathan Henrique', 'jonathan.mdr@hotmail.com', '00 99999-9999');