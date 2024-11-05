package com.sistema.escola.dto.matricula;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record FinalizarMatriculaDTO(@Min(0) @Max(10) double media) {

}
