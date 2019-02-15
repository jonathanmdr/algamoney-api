CREATE TABLE usuario (
	id BIGINT PRIMARY KEY,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL
);

CREATE TABLE permissao (
	id BIGINT PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
);

CREATE TABLE usuario_permissao (
	id_usuario BIGINT NOT NULL,
	id_permissao BIGINT NOT NULL,
	PRIMARY KEY (id_usuario, id_permissao),
	FOREIGN KEY (id_usuario) REFERENCES usuario(id),
	FOREIGN KEY (id_permissao) REFERENCES permissao(id)
);

INSERT INTO usuario (id, nome, email, senha) values (1, 'Administrador', 'admin@algamoney.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.'),
                                                    (2, 'Maria Silva', 'maria@algamoney.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO permissao (id, descricao) values (1, 'ROLE_CADASTRAR_CATEGORIA'),
                                             (2, 'ROLE_PESQUISAR_CATEGORIA'),

                                             (3, 'ROLE_CADASTRAR_PESSOA'),
                                             (4, 'ROLE_REMOVER_PESSOA'),
                                             (5, 'ROLE_PESQUISAR_PESSOA'),

                                             (6, 'ROLE_CADASTRAR_LANCAMENTO'),
                                             (7, 'ROLE_REMOVER_LANCAMENTO'),
                                             (8, 'ROLE_PESQUISAR_LANCAMENTO');

-- admin (Todas Permissões)
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (1, 1),
                                                                (1, 2),
                                                                (1, 3),
                                                                (1, 4),
                                                                (1, 5),
                                                                (1, 6),
                                                                (1, 7),
                                                                (1, 8);

-- maria (Pesmissões de Pesquisa)
INSERT INTO usuario_permissao (id_usuario, id_permissao) values (2, 2),
                                                                (2, 5),
                                                                (2, 8);