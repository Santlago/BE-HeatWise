package br.com.fiap.heatwise.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TipoPlanoValidator.class)
public @interface TipoPlano {

    String message() default "{movimentacao.tipo}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
