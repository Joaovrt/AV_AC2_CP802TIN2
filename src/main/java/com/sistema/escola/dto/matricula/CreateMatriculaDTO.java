package com.sistema.escola.dto.matricula;

import com.sistema.escola.entity.matricula.CodigoMatricula;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMatriculaDTO(@NotNull CodigoMatricula codigo, @NotBlank String aluno_id, @NotBlank String curso_id ) {

}
