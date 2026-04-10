package com.ms_pagamentos.ms_pagamentos.controller;

import com.ms_pagamentos.ms_pagamentos.dto.PagamentoDTO;
import com.ms_pagamentos.ms_pagamentos.services.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> getAll() {
        return ResponseEntity.ok(pagamentoService.findAllPagamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.findPagamentoById(id));
    }

    @PostMapping
    private ResponseEntity<PagamentoDTO> createPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(pagamentoDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(pagamentoService.savePagamento(pagamentoDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> updatePagamento(@PathVariable Long id, @RequestBody @Valid PagamentoDTO pagamentoDTO) {
        return ResponseEntity.ok(pagamentoService.updatePagamento(id, pagamentoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDTO> deletePagamento(@PathVariable Long id) {
        pagamentoService.deletePagamentoById(id);
        return ResponseEntity.noContent().build();
    }

}
