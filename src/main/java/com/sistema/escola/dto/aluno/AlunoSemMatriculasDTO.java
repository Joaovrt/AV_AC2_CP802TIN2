package com.sistema.escola.dto.aluno;

import com.sistema.escola.entity.aluno.Aluno;

public record AlunoSemMatriculasDTO(String id, String name, String email, int qtdCursosDisponiveis) {
  public static AlunoSemMatriculasDTO from(Aluno aluno) {
      return new AlunoSemMatriculasDTO(aluno.getId(), aluno.getName(), aluno.getEmail().getEmailAddress(), aluno.getQtdCursosDisponiveis());
  }
}

