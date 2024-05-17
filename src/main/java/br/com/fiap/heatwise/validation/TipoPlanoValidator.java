package br.com.fiap.heatwise.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoPlanoValidator implements ConstraintValidator<TipoPlano, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value.equals(1L) || value.equals(2L) || value.equals(3L);
    }

}
