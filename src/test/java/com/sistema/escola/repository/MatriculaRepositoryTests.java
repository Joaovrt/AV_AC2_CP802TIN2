package com.sistema.escola.repository;

import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.aluno.Email;
import com.sistema.escola.entity.curso.CodigoCurso;
import com.sistema.escola.entity.curso.Curso;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.CodigoMatricula;
import com.sistema.escola.entity.matricula.StatusMatricula;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MatriculaRepositoryTests {

  @Autowired
  private MatriculaRepository matriculaRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private CursoRepository cursoRepository;

  @Test
  public void testSave() {
    Aluno aluno = Aluno.builder()
        .name("Carlos Souza")
        .email(new Email("carlos.souza@example.com"))
        .qtdCursosDisponiveis(3)
        .build();
    aluno = alunoRepository.save(aluno);

    Curso curso = Curso.builder()
        .name("Ciências da Computação")
        .codigo(new CodigoCurso("CC101CSC9"))
        .build();
    curso = cursoRepository.save(curso);

    Matricula matricula = Matricula.builder()
        .media(8.5)
        .status(StatusMatricula.EM_ANDAMENTO)
        .codigo(new CodigoMatricula("MT01CS02"))
        .aluno(aluno)
        .curso(curso)
        .build();

    Matricula savedMatricula = matriculaRepository.save(matricula);
    assertThat(savedMatricula.getId()).isNotNull();
    assertThat(savedMatricula.getStatus()).isEqualTo(StatusMatricula.EM_ANDAMENTO);
  }

  @Test
  public void testFindAll() {
    Aluno aluno = alunoRepository.save(Aluno.builder()
        .name("Ana Lima")
        .email(new Email("ana.lima@example.com"))
        .qtdCursosDisponiveis(2)
        .build());

    Curso curso = cursoRepository.save(Curso.builder()
        .name("Engenharia Elétrica")
        .codigo(new CodigoCurso("EE202ELE3"))
        .build());

    Matricula matricula = Matricula.builder()
        .media(7.3)
        .status(StatusMatricula.FINALIZADO)
        .codigo(new CodigoMatricula("MT12EE34"))
        .aluno(aluno)
        .curso(curso)
        .build();

    matriculaRepository.save(matricula);

    List<Matricula> matriculas = matriculaRepository.findAll();
    assertThat(matriculas).hasSize(1);
  }

  @Test
  public void testFindById() {
    Aluno aluno = alunoRepository.save(Aluno.builder()
        .name("João Pedro")
        .email(new Email("joao.pedro@example.com"))
        .qtdCursosDisponiveis(1)
        .build());

    Curso curso = cursoRepository.save(Curso.builder()
        .name("Química")
        .codigo(new CodigoCurso("QM303CHE6"))
        .build());

    Matricula matricula = Matricula.builder()
        .media(9.0)
        .status(StatusMatricula.NAO_INICIADO)
        .codigo(new CodigoMatricula("MT34QM56"))
        .aluno(aluno)
        .curso(curso)
        .build();

    Matricula savedMatricula = matriculaRepository.save(matricula);
    Optional<Matricula> foundMatricula = matriculaRepository.findById(savedMatricula.getId());
    assertThat(foundMatricula).isPresent();
    assertThat(foundMatricula.get().getMedia()).isEqualTo(9.0);
  }
}
