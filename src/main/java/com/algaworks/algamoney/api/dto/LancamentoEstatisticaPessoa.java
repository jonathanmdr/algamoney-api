package com.algaworks.algamoney.api.dto;

import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.model.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class LancamentoEstatisticaPessoa {

    private TipoLancamento tipoLancamento;
    private Pessoa pessoa;
    private BigDecimal total;

}
