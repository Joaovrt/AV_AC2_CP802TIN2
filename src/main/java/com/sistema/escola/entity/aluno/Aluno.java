package com.sistema.escola.entity.aluno;

import com.sistema.escola.entity.matricula.Matricula;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import java.io.Serializable;

@Entity
@Table(name = "Alunos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Aluno implements Serializable{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID) 
  private String id;

  private String name;

  @Embedded
  private Email email;

  private int qtdCursosDisponiveis;

  @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
  private List<Matricula> matriculas;
}
