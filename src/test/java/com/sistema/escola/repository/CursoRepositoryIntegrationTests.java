package com.sistema.escola.repository;

import com.sistema.escola.entity.curso.Curso;
import com.sistema.escola.entity.curso.CodigoCurso;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CursoRepositoryIntegrationTests {

  @Autowired
  private CursoRepository cursoRepository;

  @Test
  public void testSave() {
    Curso curso = Curso.builder()
        .name("Biologia Molecular")
        .codigo(new CodigoCurso("BM654BIO2"))
        .build();

    Curso savedCurso = cursoRepository.save(curso);
    assertThat(savedCurso.getId()).isNotNull();
    assertThat(savedCurso.getName()).isEqualTo("Biologia Molecular");
  }

  @Test
  public void testFindAll() {
    Curso curso1 = Curso.builder()
        .name("Introdução à Filosofia")
        .codigo(new CodigoCurso("IF123PHI6"))
        .build();
    Curso curso2 = Curso.builder()
        .name("Lógica Matemática")
        .codigo(new CodigoCurso("LM987MTH0"))
        .build();

    cursoRepository.save(curso1);
    cursoRepository.save(curso2);

    List<Curso> cursos = cursoRepository.findAll();
    assertThat(cursos).hasSize(2);
  }

  @Test
  public void testFindById() {
    Curso curso = Curso.builder()
        .name("Engenharia Civil")
        .codigo(new CodigoCurso("EC246ENG3"))
        .build();

    Curso savedCurso = cursoRepository.save(curso);
    Optional<Curso> foundCurso = cursoRepository.findById(savedCurso.getId());
    assertThat(foundCurso).isPresent();
    assertThat(foundCurso.get().getName()).isEqualTo("Engenharia Civil");
  }
}
