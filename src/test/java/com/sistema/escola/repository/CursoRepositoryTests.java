package com.sistema.escola.repository;

import com.sistema.escola.entity.curso.Curso;
import com.sistema.escola.entity.curso.CodigoCurso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
public class CursoRepositoryTests {

  @Autowired
  private CursoRepository cursoRepository;

  @Test
  public void testSave() {
    Curso curso = Curso.builder()
        .name("Matemática Avançada")
        .codigo(new CodigoCurso("MA123CAL4"))
        .build();

    Curso savedCurso = cursoRepository.save(curso);
    assertThat(savedCurso.getId()).isNotNull();
    assertThat(savedCurso.getName()).isEqualTo("Matemática Avançada");
  }

  @Test
  public void testFindAll() {
    Curso curso1 = Curso.builder()
        .name("Física Quântica")
        .codigo(new CodigoCurso("FQ456PHY8"))
        .build();
    Curso curso2 = Curso.builder()
        .name("História Moderna")
        .codigo(new CodigoCurso("HM789HST1"))
        .build();

    cursoRepository.save(curso1);
    cursoRepository.save(curso2);

    List<Curso> cursos = cursoRepository.findAll();
    assertThat(cursos).hasSize(2);
  }

  @Test
  public void testFindById() {
    Curso curso = Curso.builder()
        .name("Química Orgânica")
        .codigo(new CodigoCurso("QO321CHE5"))
        .build();

    Curso savedCurso = cursoRepository.save(curso);
    Optional<Curso> foundCurso = cursoRepository.findById(savedCurso.getId());
    assertThat(foundCurso).isPresent();
    assertThat(foundCurso.get().getName()).isEqualTo("Química Orgânica");
  }
}
