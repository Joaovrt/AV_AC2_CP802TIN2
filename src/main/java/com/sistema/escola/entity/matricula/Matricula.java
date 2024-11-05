package com.sistema.escola.entity.matricula;

import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.curso.Curso;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "Matriculas", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "curso_id"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Matricula implements Serializable{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID) 
  private String id;

  private double media;

  private StatusMatricula status;

  @Embedded
  private CodigoMatricula codigo;
  
  @ManyToOne
  @JoinColumn(name = "aluno_id", referencedColumnName = "id")
  private Aluno aluno;

  @ManyToOne
  @JoinColumn(name = "curso_id", referencedColumnName = "id")
  private Curso curso;
}
