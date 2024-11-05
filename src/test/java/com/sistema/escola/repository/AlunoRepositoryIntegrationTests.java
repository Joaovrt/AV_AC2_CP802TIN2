package com.sistema.escola.repository;

import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.aluno.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AlunoRepositoryIntegrationTests {

  @Autowired
  private AlunoRepository alunoRepository;

  @Test
  public void testSave() {
    Aluno aluno = Aluno.builder()
        .name("Pedro")
        .email(new Email("pedro@example.com"))
        .qtdCursosDisponiveis(1)
        .build();

    Aluno savedAluno = alunoRepository.save(aluno);
    assertThat(savedAluno.getId()).isNotNull();
    assertThat(savedAluno.getName()).isEqualTo("Pedro");
  }

  @Test
  public void testFindAll() {
    Aluno aluno1 = Aluno.builder()
        .name("Julia")
        .email(new Email("julia@example.com"))
        .qtdCursosDisponiveis(1)
        .build();
    Aluno aluno2 = Aluno.builder()
        .name("Lucas")
        .email(new Email("lucas@example.com"))
        .qtdCursosDisponiveis(1)
        .build();

    alunoRepository.save(aluno1);
    alunoRepository.save(aluno2);

    List<Aluno> alunos = alunoRepository.findAll();
    assertThat(alunos).hasSize(2);
  }

  @Test
  public void testFindById() {
    Aluno aluno = Aluno.builder()
        .name("Beatriz")
        .email(new Email("beatriz@example.com"))
        .qtdCursosDisponiveis(1)
        .build();

    Aluno savedAluno = alunoRepository.save(aluno);
    Optional<Aluno> foundAluno = alunoRepository.findById(savedAluno.getId());
    assertThat(foundAluno).isPresent();
    assertThat(foundAluno.get().getName()).isEqualTo("Beatriz");
  }
}
