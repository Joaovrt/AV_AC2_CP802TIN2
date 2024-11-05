package com.sistema.escola.dto.aluno;

import com.sistema.escola.dto.matricula.MatriculaSemAlunoDTO;
import com.sistema.escola.entity.aluno.Aluno;
import java.util.List;
import java.util.stream.Collectors;

public record AlunoComMatriculasDTO(String id, String name, String email, int qtdCursosDisponiveis, List<MatriculaSemAlunoDTO> matriculas) {
  public static AlunoComMatriculasDTO from(Aluno aluno) {
      return new AlunoComMatriculasDTO(aluno.getId(), aluno.getName(), aluno.getEmail().getEmailAddress(), aluno.getQtdCursosDisponiveis(), aluno.getMatriculas().stream().map(MatriculaSemAlunoDTO::from).collect(Collectors.toList()));
  }
}

