package com.sistema.escola.repository;

import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.aluno.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AlunoRepositoryTests {

  @Autowired
  private AlunoRepository alunoRepository;

  @Test
  public void testSave() {
    Aluno aluno = Aluno.builder()
        .name("João")
        .email(new Email("joao@example.com"))
        .qtdCursosDisponiveis(1)
        .build();

    Aluno savedAluno = alunoRepository.save(aluno);
    assertThat(savedAluno.getId()).isNotNull();
    assertThat(savedAluno.getName()).isEqualTo("João");
  }

  @Test
  public void testFindAll() {
    Aluno aluno1 = Aluno.builder()
        .name("Maria")
        .email(new Email("maria@example.com"))
        .qtdCursosDisponiveis(1)
        .build();
    Aluno aluno2 = Aluno.builder()
        .name("Carlos")
        .email(new Email("carlos@example.com"))
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
        .name("Ana")
        .email(new Email("ana@example.com"))
        .qtdCursosDisponiveis(1)
        .build();

    Aluno savedAluno = alunoRepository.save(aluno);
    Optional<Aluno> foundAluno = alunoRepository.findById(savedAluno.getId());
    assertThat(foundAluno).isPresent();
    assertThat(foundAluno.get().getName()).isEqualTo("Ana");
  }
}
