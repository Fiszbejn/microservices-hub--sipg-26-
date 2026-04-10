package com.ms_pagamentos.ms_pagamentos.repositories;

import com.ms_pagamentos.ms_pagamentos.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

}
