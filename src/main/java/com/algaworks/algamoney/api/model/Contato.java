package com.algaworks.algamoney.api.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "contato")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nome;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String telefone;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idpessoa")
    private Pessoa pessoa;

}
