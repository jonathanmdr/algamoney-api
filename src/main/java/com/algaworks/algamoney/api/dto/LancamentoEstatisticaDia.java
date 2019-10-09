package com.algaworks.algamoney.api.dto;

import com.algaworks.algamoney.api.model.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class LancamentoEstatisticaDia {

    private TipoLancamento tipoLancamento;
    private LocalDate dia;
    private BigDecimal total;

}
