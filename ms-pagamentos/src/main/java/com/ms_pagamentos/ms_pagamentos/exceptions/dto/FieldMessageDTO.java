package com.ms_pagamentos.ms_pagamentos.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FieldMessageDTO {

    private String fieldName;
    private String message;

}
