package com.sistema.escola.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sistema.escola.entity.matricula.CodigoMatricula;

import org.junit.jupiter.api.Test;

public class MatriculaEntityTests {

  @Test
  public void deveCriarMatriculaComCodigoValido() {
    String codigoValido = "AB12CD34";

    CodigoMatricula codigoMatricula = new CodigoMatricula(codigoValido);

    assertEquals(codigoValido, codigoMatricula.getCodigo());
  }

  @Test
  public void deveLancarExcecaoParaCodigoInvalido() {
    String[] codigosInvalidos = {
        "AB123CD4",   // Falta um dígito
        "AB1CD234",   // Estrutura numérica incorreta
        "AB12C34",    // Falta uma letra
        "A12CD34",    // Letra inicial ausente
        "AB12CD345",  // Dígito extra
        ""            // String vazia
    };

    for (String codigoInvalido : codigosInvalidos) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CodigoMatricula(codigoInvalido);
        });

        assertEquals("Código inválido", exception.getMessage());
    }
  }
}
