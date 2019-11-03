package com.algaworks.algamoney.api.model;

import lombok.Getter;

public enum TipoLancamento {

    RECEITA("Receita"),
    DESPESA("Despesa");

    @Getter
    private final String descricao;

    TipoLancamento(String descricao) {
        this.descricao = descricao;
    }

}
