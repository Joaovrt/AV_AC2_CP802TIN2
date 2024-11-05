package com.sistema.escola.dto.matricula;

import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.StatusMatricula;

public record MatriculaSemAlunoDTO(String id, double media, StatusMatricula status, CursoSemMatriculasDTO curso) {
  public static MatriculaSemAlunoDTO from(Matricula matricula) {
      return new MatriculaSemAlunoDTO(matricula.getId(), matricula.getMedia(), matricula.getStatus(), CursoSemMatriculasDTO.from(matricula.getCurso()));
  }
}
