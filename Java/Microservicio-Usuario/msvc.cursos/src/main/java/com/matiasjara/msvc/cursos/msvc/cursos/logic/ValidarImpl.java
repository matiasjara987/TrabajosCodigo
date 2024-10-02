package com.matiasjara.msvc.cursos.msvc.cursos.logic;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class ValidarImpl implements Validar{
    @Override
    public ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach( e -> {
            errors.put(e.getField(), "EL campo "+ e.getField() +" " + e.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);

    }
}
