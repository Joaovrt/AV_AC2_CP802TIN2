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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MatriculaRepositoryIntegrationTests {

  @Autowired
  private MatriculaRepository matriculaRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private CursoRepository cursoRepository;

  @Test
  public void testSave() {
    Aluno aluno = Aluno.builder()
        .name("Mariana Silva")
        .email(new Email("mariana.silva@example.com"))
        .qtdCursosDisponiveis(5)
        .build();
    aluno = alunoRepository.save(aluno);

    Curso curso = Curso.builder()
        .name("Medicina")
        .codigo(new CodigoCurso("MD456MED7"))
        .build();
    curso = cursoRepository.save(curso);

    Matricula matricula = Matricula.builder()
        .media(88.0)
        .status(StatusMatricula.EM_ANDAMENTO)
        .codigo(new CodigoMatricula("MT78MD90"))
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
        .name("Lucas Martins")
        .email(new Email("lucas.martins@example.com"))
        .qtdCursosDisponiveis(4)
        .build());

    Curso curso = cursoRepository.save(Curso.builder()
        .name("Arquitetura")
        .codigo(new CodigoCurso("AR567ARC0"))
        .build());

    Matricula matricula = Matricula.builder()
        .media(75.5)
        .status(StatusMatricula.FINALIZADO)
        .codigo(new CodigoMatricula("MT56AR78"))
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
        .name("Paula Ferreira")
        .email(new Email("paula.ferreira@example.com"))
        .qtdCursosDisponiveis(2)
        .build());

    Curso curso = cursoRepository.save(Curso.builder()
        .name("Direito")
        .codigo(new CodigoCurso("DR678LAW5"))
        .build());

    Matricula matricula = Matricula.builder()
        .media(82.0)
        .status(StatusMatricula.NAO_INICIADO)
        .codigo(new CodigoMatricula("MT90DR12"))
        .aluno(aluno)
        .curso(curso)
        .build();

    Matricula savedMatricula = matriculaRepository.save(matricula);
    Optional<Matricula> foundMatricula = matriculaRepository.findById(savedMatricula.getId());
    assertThat(foundMatricula).isPresent();
    assertThat(foundMatricula.get().getStatus()).isEqualTo(StatusMatricula.NAO_INICIADO);
  }
}
