package com.ms_pagamentos.ms_pagamentos.services;

import com.ms_pagamentos.ms_pagamentos.repositories.PagamentoRepository;
import com.ms_pagamentos.ms_pagamentos.dto.PagamentoDTO;
import com.ms_pagamentos.ms_pagamentos.entities.Pagamento;
import com.ms_pagamentos.ms_pagamentos.entities.Status;
import com.ms_pagamentos.ms_pagamentos.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Transactional(readOnly = true)
    public List<PagamentoDTO> findAllPagamentos() {
        return pagamentoRepository.findAll().stream().map(PagamentoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public PagamentoDTO findPagamentoById(Long id) {
        return new PagamentoDTO(pagamentoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado. ID: " + id)));
    }

    @Transactional
    public PagamentoDTO savePagamento(@Valid PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = new Pagamento();
        mapperDtoToPagamento(pagamentoDTO, pagamento);
        pagamento.setStatus(Status.CRIADO);
        pagamento = pagamentoRepository.save(pagamento);
        return new PagamentoDTO(pagamento);
    }

    private void mapperDtoToPagamento(PagamentoDTO pagamentoDTO, Pagamento pagamento) {
        pagamento.setValor(pagamentoDTO.getValor());
        pagamento.setNome(pagamentoDTO.getNome());
        pagamento.setNumeroCartao(pagamentoDTO.getNumeroCartao());
        pagamento.setValidade(pagamentoDTO.getValidade());
        pagamento.setCodigoSeguranca(pagamentoDTO.getCodigoSeguranca());
        pagamento.setPedidoId(pagamentoDTO.getPedidoId());
    }

    @Transactional
    public PagamentoDTO updatePagamento(Long id, @Valid PagamentoDTO pagamentoDTO) {
        try {
            Pagamento pagamento = pagamentoRepository.getReferenceById(id);
            mapperDtoToPagamento(pagamentoDTO, pagamento);
            pagamento.setStatus(pagamentoDTO.getStatus());
            pagamento = pagamentoRepository.save(pagamento);
            return new PagamentoDTO(pagamento);
        }catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    @Transactional
    public void deletePagamentoById(Long id) {
        if(!pagamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
        pagamentoRepository.deleteById(id);
    }
}
