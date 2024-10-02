package com.matiasjara.spring.cloud.msvc.usuarios.logic;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class validacionImpl implements Validacion {
    @Override
    public ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "EL campo "+ err.getField() +" " + err.getDefaultMessage());
        });
        return  ResponseEntity.badRequest().body(errors);
    }

}
