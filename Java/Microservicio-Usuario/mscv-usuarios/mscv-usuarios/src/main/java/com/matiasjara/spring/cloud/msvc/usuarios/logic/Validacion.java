package com.matiasjara.spring.cloud.msvc.usuarios.logic;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface Validacion {
    public ResponseEntity<Map<String, String>> validar(BindingResult result);
}
