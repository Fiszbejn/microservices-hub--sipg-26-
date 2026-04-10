package com.ms_pagamentos.ms_pagamentos.service.tests;

import com.ms_pagamentos.ms_pagamentos.entities.Pagamento;
import com.ms_pagamentos.ms_pagamentos.entities.Status;

import java.math.BigDecimal;

//Classe para instanciar objetos
public class Factory {

    public static Pagamento createPagamento() {
        Pagamento pagamento = new Pagamento(1L, BigDecimal.valueOf(32.25), "Brienne de Tarth", "3654876547890987", "07/15", "345", Status.CRIADO, 1L);
        return pagamento;
    }

    public static Pagamento createPagamentoSemId() {
        Pagamento pagamento = createPagamento();
        pagamento.setId(null);
        return pagamento;
    }

}
