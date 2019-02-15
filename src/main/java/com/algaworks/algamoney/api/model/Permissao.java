package com.algaworks.algamoney.api.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "permissao")
public class Permissao {

    @Id
    private Long id;
    private String descricao;

}
