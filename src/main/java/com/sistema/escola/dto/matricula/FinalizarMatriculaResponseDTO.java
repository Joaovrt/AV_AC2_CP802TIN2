package com.sistema.escola.dto.matricula;

import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.StatusMatricula;

public record FinalizarMatriculaResponseDTO(String id, double media, StatusMatricula status, AlunoSemMatriculasDTO aluno, CursoSemMatriculasDTO curso, String mensagem) {
  public static FinalizarMatriculaResponseDTO from(Matricula matricula, String mensagem) {
      return new FinalizarMatriculaResponseDTO(matricula.getId(), matricula.getMedia(), matricula.getStatus() ,AlunoSemMatriculasDTO.from(matricula.getAluno()), CursoSemMatriculasDTO.from(matricula.getCurso()), mensagem);
  }
}
