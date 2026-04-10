package com.ms_pagamentos.ms_pagamentos.service;

import ch.qos.logback.core.testUtil.MockInitialContext;
import com.ms_pagamentos.ms_pagamentos.dto.PagamentoDTO;
import com.ms_pagamentos.ms_pagamentos.entities.Pagamento;
import com.ms_pagamentos.ms_pagamentos.exceptions.ResourceNotFoundException;
import com.ms_pagamentos.ms_pagamentos.repositories.PagamentoRepository;
import com.ms_pagamentos.ms_pagamentos.service.tests.Factory;
import com.ms_pagamentos.ms_pagamentos.services.PagamentoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @Mock //Cria um dublê (mock) do repository para isolar o teste
    private PagamentoRepository pagamentoRepository;

    @InjectMocks //Cria a instancia real do service (SUT) e injeta os mocks nela
    private PagamentoService pagamentoService;

    private Pagamento pagamento;

    //Não acessa DB || Preparando os dados - variáveis
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
        //executado antes de cada classe
    void setUp() {
        existingId = 1L;
        nonExistingId = Long.MAX_VALUE;

        pagamento = Factory.createPagamento();
    }

    @Test
    void deletePagamentoByIdShouldDeleteWhenIdExists() {

        //Arrange - prepara o comportamento do mock (stubbing)
        Mockito.when(pagamentoRepository.existsById(existingId)).thenReturn(true);

        pagamentoService.deletePagamentoById(existingId);

        //verify(...) = verifica se o mock foi chamado
        //Verifica que o mock pagamentoRepository recebeu uma chamada ao método existsById.
        Mockito.verify(pagamentoRepository).existsById(existingId);
        //Verifica se o metodo deleteById do repository foi chamado exatamente 1 vez (padrão)
        Mockito.verify(pagamentoRepository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("deletePagamentoById deveria lançar ResourceNotFoundException quando o Id não existir")
    void deletePagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        //Arrange
        Mockito.when(pagamentoRepository.existsById(nonExistingId)).thenReturn(false);
        //Act + Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           pagamentoService.deletePagamentoById(nonExistingId);
        });

        //Verificações (behavior)
        Mockito.verify(pagamentoRepository).existsById(nonExistingId);
        //never() = equivalente a times(0) -> esse método não pode ter sido chamado nenhuma vez
        //anyLong() é um matcher (coringa): aceita qualquer valor long/Long
        Mockito.verify(pagamentoRepository, Mockito.never()).deleteById(Mockito.anyLong());

    }

    @Test
    void findPagamentoByIdShouldReturnPagamentoDTOWhenIdExists() {

        //Arrange
        Mockito.when(pagamentoRepository.findById(existingId)).thenReturn(Optional.of(pagamento));

        //Act
        PagamentoDTO result = pagamentoService.findPagamentoById(existingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());

        Mockito.verify(pagamentoRepository).findById(existingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    void findPagamentoByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Mockito.when(pagamentoRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            pagamentoService.findPagamentoById(nonExistingId);
        });

        Mockito.verify(pagamentoRepository).findById(nonExistingId);
        Mockito.verifyNoMoreInteractions(pagamentoRepository);

    }

    @Test
    @DisplayName("Dado parâmetros válidos e Id nulo quando chamar Salvar Pagamento então deve gerar Id e persistir um Pagamento")
    void givenValidParamsAndIdIsNull_whenSave_thenShouldPersistPagamento() {
        //Arrange
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        PagamentoDTO inputDto = new PagamentoDTO(pagamento);

        //Act
        PagamentoDTO result = pagamentoService.savePagamento(inputDto);

        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(pagamento.getId(), result.getId());

        //Verify
        Mockito.verify(pagamentoRepository).save(any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

    @Test
    void updatePagamentoShouldReturnPagamentoDTOWhenIdExists() {
        //Arrange
        Long id = pagamento.getId();
        Mockito.when(pagamentoRepository.getReferenceById(id)).thenReturn(pagamento);
        Mockito.when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        //Act
        PagamentoDTO result = pagamentoService.updatePagamento(id, new PagamentoDTO(pagamento));

        //Assert and Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(pagamento.getValor(), result.getValor());
        Mockito.verify(pagamentoRepository).getReferenceById(id);
        Mockito.verify(pagamentoRepository).save(Mockito.any(Pagamento.class));
        Mockito.verifyNoMoreInteractions(pagamentoRepository);
    }

}
