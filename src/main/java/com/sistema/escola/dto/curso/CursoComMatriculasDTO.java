package com.sistema.escola.dto.curso;
import com.sistema.escola.dto.matricula.MatriculaSemCursoDTO;
import com.sistema.escola.entity.curso.Curso;
import java.util.List;
import java.util.stream.Collectors;

public record CursoComMatriculasDTO(String id, String name, String codigo, List<MatriculaSemCursoDTO> matriculas) {
  public static CursoComMatriculasDTO from(Curso curso) {
      return new CursoComMatriculasDTO(curso.getId(), curso.getName(), curso.getCodigo().getCodigo(), curso.getMatriculas().stream().map(MatriculaSemCursoDTO::from).collect(Collectors.toList()));
  }
}
