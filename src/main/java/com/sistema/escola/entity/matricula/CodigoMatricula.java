package com.sistema.escola.entity.matricula;

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
public class CodigoMatricula {
  @Column(unique = true)
  private String codigo;

  protected CodigoMatricula() {}

  public CodigoMatricula(String codigo) {
      if (codigo == null || !codigo.matches("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{2}$")) {
          throw new IllegalArgumentException("Código inválido");
      }
      this.codigo = codigo;
  }
}
