package com.sistema.escola.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.aluno.Email;

public class AlunoEntityTests {

  @Test
  public void deveCriarAlunoComEmailValido() {
    String nome = "João Silva";
    String emailValido = "joao.silva@escola.com";
    Email email = new Email(emailValido);

    Aluno aluno = Aluno.builder()
            .name(nome)
            .email(email)
            .qtdCursosDisponiveis(1)
            .build();

    assertEquals(nome, aluno.getName());
    assertEquals(emailValido, aluno.getEmail().getEmailAddress());
    assertEquals(1, aluno.getQtdCursosDisponiveis());
  }

  @Test
  public void deveLancarExcecaoParaEmailInvalido() {
    String emailInvalido = "joao.silva";

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new Email(emailInvalido);
    });

    assertEquals("Email inválido", exception.getMessage());
  }
}
