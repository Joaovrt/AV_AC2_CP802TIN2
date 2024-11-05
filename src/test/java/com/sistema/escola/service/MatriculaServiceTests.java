package com.sistema.escola.service;

import com.sistema.escola.dto.matricula.CreateMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaResponseDTO;
import com.sistema.escola.dto.matricula.MatriculaResponseDTO;
import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.entity.aluno.Email;
import com.sistema.escola.entity.curso.CodigoCurso;
import com.sistema.escola.entity.curso.Curso;
import com.sistema.escola.entity.matricula.CodigoMatricula;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.StatusMatricula;
import com.sistema.escola.exceptions.ResourceConflictException;
import com.sistema.escola.exceptions.ResourceNotFoundException;
import com.sistema.escola.repository.AlunoRepository;
import com.sistema.escola.repository.CursoRepository;
import com.sistema.escola.repository.MatriculaRepository;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatriculaServiceTests {
  @InjectMocks
  private MatriculaService matriculaService;

  @Mock
  private MatriculaRepository matriculaRepository;

  @Mock
  private AlunoRepository alunoRepository;

  @Mock
  private CursoRepository cursoRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateMatricula_AlunoNotFound() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    CreateMatriculaDTO dto = new CreateMatriculaDTO(codigoMatricula,"alunoId", "cursoId");
    when(alunoRepository.findById(dto.aluno_id())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> matriculaService.createMatricula(dto));
  }

  @Test
  void testCreateMatricula_AlunoNoCoursesAvailable() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Aluno aluno = Aluno.builder().qtdCursosDisponiveis(0).build();
    CreateMatriculaDTO dto = new CreateMatriculaDTO(codigoMatricula,"alunoId", "cursoId");
    when(alunoRepository.findById(dto.aluno_id())).thenReturn(Optional.of(aluno));

    assertThrows(ResourceConflictException.class, () -> matriculaService.createMatricula(dto));
  }

  @Test
  void testCreateMatricula_CursoNotFound() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Aluno aluno = Aluno.builder().qtdCursosDisponiveis(1).build();
    CreateMatriculaDTO dto = new CreateMatriculaDTO(codigoMatricula,"alunoId", "cursoId");

    when(alunoRepository.findById(dto.aluno_id())).thenReturn(Optional.of(aluno));
    when(cursoRepository.findById(dto.curso_id())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> matriculaService.createMatricula(dto));
  }

  @Test
  void testCreateMatricula_DataIntegrityViolationException() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Aluno aluno = Aluno.builder().qtdCursosDisponiveis(1).build();
    Curso curso = Curso.builder().build();
    CreateMatriculaDTO dto = new CreateMatriculaDTO(codigoMatricula,"alunoId", "cursoId");

    when(alunoRepository.findById(dto.aluno_id())).thenReturn(Optional.of(aluno));
    when(cursoRepository.findById(dto.curso_id())).thenReturn(Optional.of(curso));
    when(matriculaRepository.save(any())).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

    assertThrows(ResourceConflictException.class, () -> matriculaService.createMatricula(dto));
  }

  @Test
  void testCreateMatricula_UnhandledException() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Aluno aluno = Aluno.builder().qtdCursosDisponiveis(1).build();
    Curso curso = Curso.builder().build();
    CreateMatriculaDTO dto = new CreateMatriculaDTO(codigoMatricula,"alunoId", "cursoId");

    when(alunoRepository.findById(dto.aluno_id())).thenReturn(Optional.of(aluno));
    when(cursoRepository.findById(dto.curso_id())).thenReturn(Optional.of(curso));
    when(matriculaRepository.save(any())).thenThrow(new RuntimeException("Unexpected error"));

    assertThrows(RuntimeException.class, () -> matriculaService.createMatricula(dto));
  }

  @Test
  void testCreateMatricula_Success() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    CreateMatriculaDTO dto = new CreateMatriculaDTO(codigoMatricula,"4f8ae9ff-b9f0-496d-8093-cee5edec5489", "cursoId");
    Matricula matricula = Matricula.builder()
      .codigo(dto.codigo())
      .media(0)
      .status(StatusMatricula.NAO_INICIADO)
      .aluno(aluno)
      .curso(curso)
      .build();

    when(alunoRepository.findById(dto.aluno_id())).thenReturn(Optional.of(aluno));
    when(cursoRepository.findById(dto.curso_id())).thenReturn(Optional.of(curso));
    when(matriculaRepository.save(any())).thenReturn(matricula);

    MatriculaResponseDTO result = matriculaService.createMatricula(dto);

    assertEquals(MatriculaResponseDTO.from(matricula), result);
  }

  @Test
  void testListAllMatriculas() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    Matricula matricula = Matricula.builder()
      .id("9e7222fd-2a76-4b4a-bb83-687232e3cca9")
      .codigo(codigoMatricula)
      .status(StatusMatricula.NAO_INICIADO)
      .aluno(aluno)
      .curso(curso)
      .media(0)
      .build();

    when(matriculaRepository.findAll()).thenReturn(List.of(matricula));
    assertFalse(matriculaService.listAllMatriculas().isEmpty());
  }

  @Test
  void testGetMatricula_NotFound() {
    when(matriculaRepository.findById("id")).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> matriculaService.getMatricula("id"));
  }

  @Test
  void testGetMatricula_Success() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    Matricula matricula = Matricula.builder()
      .id("9e7222fd-2a76-4b4a-bb83-687232e3cca9")
      .codigo(codigoMatricula)
      .status(StatusMatricula.NAO_INICIADO)
      .aluno(aluno)
      .curso(curso)
      .media(0)
      .build();
      
    when(matriculaRepository.findById("9e7222fd-2a76-4b4a-bb83-687232e3cca9")).thenReturn(Optional.of(matricula));
    MatriculaResponseDTO result = matriculaService.getMatricula("9e7222fd-2a76-4b4a-bb83-687232e3cca9");

    assertEquals(MatriculaResponseDTO.from(matricula), result);
  }

  @Test
  void testIniciarMatricula_NotFound() {
    when(matriculaRepository.findById("id")).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> matriculaService.iniciarMatricula("id"));
  }

  @Test
  void testIniciarMatricula_InvalidStatus() {
    Matricula matricula = Matricula.builder().status(StatusMatricula.EM_ANDAMENTO).build();
    when(matriculaRepository.findById("id")).thenReturn(Optional.of(matricula));

    assertThrows(ResourceConflictException.class, () -> matriculaService.iniciarMatricula("id"));
  }

  @Test
  void testIniciarMatricula_Success() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    Matricula matricula = Matricula.builder()
      .id("9e7222fd-2a76-4b4a-bb83-687232e3cca9")
      .codigo(codigoMatricula)
      .status(StatusMatricula.NAO_INICIADO)
      .aluno(aluno)
      .curso(curso)
      .media(0)
      .build();
    when(matriculaRepository.findById("9e7222fd-2a76-4b4a-bb83-687232e3cca9")).thenReturn(Optional.of(matricula));
    when(matriculaRepository.save(any())).thenReturn(matricula);

    MatriculaResponseDTO result = matriculaService.iniciarMatricula("9e7222fd-2a76-4b4a-bb83-687232e3cca9");
    matricula.setStatus(StatusMatricula.EM_ANDAMENTO);

    assertEquals(MatriculaResponseDTO.from(matricula), result);
  }

  @Test
  void testDesistirMatricula_NotFound() {
    when(matriculaRepository.findById("id")).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> matriculaService.desistirMatricula("id"));
  }

  @Test
  void testDesistirMatricula_InvalidStatus() {
    Matricula matricula = Matricula.builder().status(StatusMatricula.NAO_INICIADO).build();
    when(matriculaRepository.findById("id")).thenReturn(Optional.of(matricula));

    assertThrows(ResourceConflictException.class, () -> matriculaService.desistirMatricula("id"));
  }

  @Test
  void testDesistirMatricula_Success() {
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    Matricula matricula = Matricula.builder()
      .id("9e7222fd-2a76-4b4a-bb83-687232e3cca9")
      .codigo(codigoMatricula)
      .status(StatusMatricula.EM_ANDAMENTO)
      .aluno(aluno)
      .curso(curso)
      .media(0)
      .build();
    when(matriculaRepository.findById("9e7222fd-2a76-4b4a-bb83-687232e3cca9")).thenReturn(Optional.of(matricula));
    when(matriculaRepository.save(any())).thenReturn(matricula);

    MatriculaResponseDTO result = matriculaService.desistirMatricula("9e7222fd-2a76-4b4a-bb83-687232e3cca9");
    matricula.setStatus(StatusMatricula.NAO_INICIADO);

    assertEquals(MatriculaResponseDTO.from(matricula), result);
  }

  @Test
  void testFinalizarMatricula_NotFound() {
    FinalizarMatriculaDTO finalizarMatriculaDTO = new FinalizarMatriculaDTO(7.0);
    when(matriculaRepository.findById("id")).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> matriculaService.finalizarMatricula("id", finalizarMatriculaDTO));
  }

  @Test
  void testFinalizarMatricula_InvalidStatus() {
    FinalizarMatriculaDTO finalizarMatriculaDTO = new FinalizarMatriculaDTO(7.0);
    Matricula matricula = Matricula.builder().status(StatusMatricula.NAO_INICIADO).build();
    when(matriculaRepository.findById("id")).thenReturn(Optional.of(matricula));

    assertThrows(ResourceConflictException.class, () -> matriculaService.finalizarMatricula("id", finalizarMatriculaDTO));
  }

  @Test
  void testFinalizarMatricula_ComBeneficio() {
    FinalizarMatriculaDTO finalizarMatriculaDTO = new FinalizarMatriculaDTO(7.1);
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    Matricula matricula = Matricula.builder()
      .id("9e7222fd-2a76-4b4a-bb83-687232e3cca9")
      .codigo(codigoMatricula)
      .status(StatusMatricula.EM_ANDAMENTO)
      .aluno(aluno)
      .curso(curso)
      .media(0)
      .build();

    when(matriculaRepository.findById("9e7222fd-2a76-4b4a-bb83-687232e3cca9")).thenReturn(Optional.of(matricula));
    when(matriculaRepository.save(any())).thenReturn(matricula);
    when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));

    FinalizarMatriculaResponseDTO result = matriculaService.finalizarMatricula("9e7222fd-2a76-4b4a-bb83-687232e3cca9", finalizarMatriculaDTO);
    matricula.setStatus(StatusMatricula.FINALIZADO);
    matricula.setMedia(finalizarMatriculaDTO.media());
    aluno.setQtdCursosDisponiveis(4);

    assertEquals(FinalizarMatriculaResponseDTO.from(matricula,"Benefício de mais 3 cursos adquirido!"), result);
  }

  @Test
  void testFinalizarMatricula_SemBeneficio() {
    FinalizarMatriculaDTO finalizarMatriculaDTO = new FinalizarMatriculaDTO(7.);
    CodigoMatricula codigoMatricula = new CodigoMatricula("AA00AA00");
    Email email1 = new Email("john@example.com");
    Aluno aluno = Aluno.builder()
      .id("055d72b4-536e-43fd-8570-4900ba3440f7")
      .name("John Doe")
      .email(email1)
      .qtdCursosDisponiveis(1)
      .matriculas(new ArrayList<>())
      .build();
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();
    Matricula matricula = Matricula.builder()
      .id("9e7222fd-2a76-4b4a-bb83-687232e3cca9")
      .codigo(codigoMatricula)
      .status(StatusMatricula.EM_ANDAMENTO)
      .aluno(aluno)
      .curso(curso)
      .media(0)
      .build();

    when(matriculaRepository.findById("9e7222fd-2a76-4b4a-bb83-687232e3cca9")).thenReturn(Optional.of(matricula));
    when(matriculaRepository.save(any())).thenReturn(matricula);
    when(alunoRepository.findById(aluno.getId())).thenReturn(Optional.of(aluno));

    FinalizarMatriculaResponseDTO result = matriculaService.finalizarMatricula("9e7222fd-2a76-4b4a-bb83-687232e3cca9", finalizarMatriculaDTO);
    matricula.setStatus(StatusMatricula.FINALIZADO);
    matricula.setMedia(finalizarMatriculaDTO.media());
    aluno.setQtdCursosDisponiveis(1);

    assertEquals(FinalizarMatriculaResponseDTO.from(matricula,"Nota mínima para o benefício não atingida. Assista ao vídeo listado na aba 'Revisão'."), result);
  }
}
