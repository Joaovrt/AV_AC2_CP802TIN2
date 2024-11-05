package com.sistema.escola.entity;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.sistema.escola.entity.curso.CodigoCurso;

public class CursoEntityTests {

  @Test
  public void deveCriarCursoComCodigoValido() {
    String codigoValido = "CP802TIN2";

    CodigoCurso codigoCurso = new CodigoCurso(codigoValido);

    assertEquals(codigoValido, codigoCurso.getCodigo());
  }

  @Test
  public void deveLancarExcecaoParaCodigoInvalido() {
    String[] codigosInvalidos = {
        "AB12XYZ1",   // Faltando um dígito
        "A123XYZ1",   // Letra inicial ausente
        "AB123XY1",   // Falta uma letra
        "AB123XYZ12", // Dígito extra
        "123ABCXYZ1", // Números no lugar errado
        ""            // String vazia
    };

    for (String codigoInvalido : codigosInvalidos) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CodigoCurso(codigoInvalido);
        });

        assertEquals("Código inválido", exception.getMessage());
    }
  }
}

