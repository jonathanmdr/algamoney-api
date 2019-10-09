package com.algaworks.algamoney.api.dto;

import com.algaworks.algamoney.api.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class LancamentoEstatisticaCategoria {

    private Categoria categoria;
    private BigDecimal total;

}
