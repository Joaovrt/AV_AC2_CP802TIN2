package com.sistema.escola.dto.aluno;

import com.sistema.escola.entity.aluno.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAlunoDTO(@NotBlank String name, @NotNull Email email) {

}
