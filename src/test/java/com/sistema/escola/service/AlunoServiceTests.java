package com.sistema.escola.service;

import com.sistema.escola.dto.aluno.AlunoComMatriculasDTO;
import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.aluno.CreateAlunoDTO;
import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.aluno.Email;
import com.sistema.escola.exceptions.ResourceConflictException;
import com.sistema.escola.exceptions.ResourceNotFoundException;
import com.sistema.escola.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlunoServiceTests {

  @Mock
  private AlunoRepository alunoRepository;

  @InjectMocks
  private AlunoService alunoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createAluno_Sucesso() {
    Email email = new Email("john@example.com");
    CreateAlunoDTO createAlunoDTO = new CreateAlunoDTO("John Doe", email);

    Aluno aluno = Aluno.builder()
      .name(createAlunoDTO.name())
      .email(createAlunoDTO.email())
      .qtdCursosDisponiveis(1)
      .build();

    when(alunoRepository.save(aluno)).thenReturn(aluno);

    AlunoSemMatriculasDTO result = alunoService.createAluno(createAlunoDTO);

    assertEquals("John Doe", result.name());
    assertEquals("john@example.com", result.email());
    verify(alunoRepository, times(1)).save(aluno);
  }

  @Test
  void createAluno_ConflitoDeEmail() {
    Email email = new Email("john@example.com");
    CreateAlunoDTO createAlunoDTO = new CreateAlunoDTO("John Doe", email);

    Aluno aluno = Aluno.builder()
      .name(createAlunoDTO.name())
      .email(createAlunoDTO.email())
      .qtdCursosDisponiveis(1)
      .build();

    when(alunoRepository.save(aluno)).thenThrow(new DataIntegrityViolationException(""));

    Exception exception = assertThrows(ResourceConflictException.class, () -> {
      alunoService.createAluno(createAlunoDTO);
    });

    assertEquals("Outro aluno já está cadastrado com esse e-mail: john@example.com", exception.getMessage());
    verify(alunoRepository, times(1)).save(aluno);
  }

  @Test
  void createAluno_ErroInesperado() {
    Email email = new Email("john@example.com");
    CreateAlunoDTO createAlunoDTO = new CreateAlunoDTO("John Doe", email);

    Aluno aluno = Aluno.builder()
      .name(createAlunoDTO.name())
      .email(createAlunoDTO.email())
      .qtdCursosDisponiveis(1)
      .build();

    when(alunoRepository.save(aluno)).thenThrow(new RuntimeException("Unexpected Error"));

    Exception exception = assertThrows(RuntimeException.class, () -> {
        alunoService.createAluno(createAlunoDTO);
    });

    assertEquals("Erro inesperado ao criar aluno.", exception.getMessage());
    verify(alunoRepository, times(1)).save(aluno);
  }

  @Test
  void listAllAlunos_Sucesso() {
    Email email1 = new Email("john@example.com");
    Email email2 = new Email("jane@example.com");
    Aluno aluno1 = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .build();
    Aluno aluno2 = Aluno.builder()
    .id("c314a953-20fd-4724-8753-e9386e23e0ee")
      .name("Jane Doe")
      .email(email2)
      .qtdCursosDisponiveis(1)
      .build();

    when(alunoRepository.findAll()).thenReturn(List.of(aluno1, aluno2));

    List<AlunoSemMatriculasDTO> result = alunoService.listAllAlunos();

    assertEquals(2, result.size());
    assertEquals("John Doe", result.get(0).name());
    assertEquals("jane@example.com", result.get(1).email());
    verify(alunoRepository, times(1)).findAll();
  }

  @Test
  void getAluno_Sucesso() {
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();

    when(alunoRepository.findById("055d72b4-536e-43fd-8570-4900ba3440f7")).thenReturn(Optional.of(aluno));

    AlunoComMatriculasDTO result = alunoService.getAluno("055d72b4-536e-43fd-8570-4900ba3440f7");

    assertEquals("John Doe", result.name());
    assertEquals("john@example.com", result.email());
    verify(alunoRepository, times(1)).findById("055d72b4-536e-43fd-8570-4900ba3440f7");
  }

  @Test
  void getAluno_NaoEncontrado() {
    when(alunoRepository.findById("0")).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
        alunoService.getAluno("0");
    });

    assertEquals("Aluno não encontrado com o id: 0", exception.getMessage());
    verify(alunoRepository, times(1)).findById("0");
  }
}
