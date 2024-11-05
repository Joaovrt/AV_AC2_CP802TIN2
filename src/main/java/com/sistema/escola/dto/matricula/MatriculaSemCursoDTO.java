package com.sistema.escola.dto.matricula;

import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.StatusMatricula;

public record MatriculaSemCursoDTO(String id, double media, StatusMatricula status, AlunoSemMatriculasDTO aluno) {
  public static MatriculaSemCursoDTO from(Matricula matricula) {
      return new MatriculaSemCursoDTO(matricula.getId(), matricula.getMedia(), matricula.getStatus() ,AlunoSemMatriculasDTO.from(matricula.getAluno()));
  }
}
