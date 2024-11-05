package com.sistema.escola.entity.curso;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CodigoCurso {
  @Column(unique = true)
  private String codigo;

  protected CodigoCurso() {}

  public CodigoCurso(String codigo) {
      if (codigo == null || !codigo.matches("^[A-Z]{2}[0-9]{3}[A-Z]{3}[0-9]$")) {
          throw new IllegalArgumentException("Código inválido");
      }
      this.codigo = codigo;
  }
}
