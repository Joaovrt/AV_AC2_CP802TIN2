package com.sistema.escola.dto.matricula;

import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.StatusMatricula;

public record MatriculaResponseDTO(String id, double media, StatusMatricula status, String codigo, AlunoSemMatriculasDTO aluno, CursoSemMatriculasDTO curso) {
  public static MatriculaResponseDTO from(Matricula matricula) {
      return new MatriculaResponseDTO(matricula.getId(), matricula.getMedia(), matricula.getStatus(), matricula.getCodigo().getCodigo(),AlunoSemMatriculasDTO.from(matricula.getAluno()), CursoSemMatriculasDTO.from(matricula.getCurso()));
  }
}
