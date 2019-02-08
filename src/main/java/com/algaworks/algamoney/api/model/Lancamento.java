package com.algaworks.algamoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "lancamento")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    private BigDecimal valor;
    private String observacao;

    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @ManyToOne
    @JoinColumn(name = "idcategoria")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "idpessoa")
    private Pessoa pessoa;

}
