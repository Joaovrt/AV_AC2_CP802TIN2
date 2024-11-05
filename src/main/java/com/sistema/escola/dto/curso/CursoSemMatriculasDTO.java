package com.sistema.escola.dto.curso;
import com.sistema.escola.entity.curso.Curso;

public record CursoSemMatriculasDTO(String id, String name, String codigo) {
  public static CursoSemMatriculasDTO from(Curso curso) {
      return new CursoSemMatriculasDTO(curso.getId(), curso.getName(), curso.getCodigo().getCodigo());
  }
}
