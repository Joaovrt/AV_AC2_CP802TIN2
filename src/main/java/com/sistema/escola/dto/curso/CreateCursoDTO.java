package com.sistema.escola.dto.curso;

import com.sistema.escola.entity.curso.CodigoCurso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCursoDTO(@NotBlank String name, @NotNull CodigoCurso codigo) {

}
