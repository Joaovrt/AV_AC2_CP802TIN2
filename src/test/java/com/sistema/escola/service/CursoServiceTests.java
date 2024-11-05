package com.sistema.escola.service;

import com.sistema.escola.dto.curso.CreateCursoDTO;
import com.sistema.escola.dto.curso.CursoComMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.entity.curso.CodigoCurso;
import com.sistema.escola.entity.curso.Curso;
import com.sistema.escola.exceptions.ResourceConflictException;
import com.sistema.escola.exceptions.ResourceNotFoundException;
import com.sistema.escola.repository.CursoRepository;
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

public class CursoServiceTests {

  @Mock
  private CursoRepository cursoRepository;

  @InjectMocks
  private CursoService cursoService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createCurso_Sucesso() {
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    CreateCursoDTO createCursoDTO = new CreateCursoDTO("Curso de Matemática", codigoCurso);

    Curso curso = Curso.builder()
      .name(createCursoDTO.name())
      .codigo(createCursoDTO.codigo())
      .build();

    when(cursoRepository.save(curso)).thenReturn(curso);

    CursoSemMatriculasDTO result = cursoService.createCurso(createCursoDTO);

    assertEquals("Curso de Matemática", result.name());
    assertEquals("MT106TIN1", result.codigo());
    verify(cursoRepository, times(1)).save(curso);
  }

  @Test
  void createCurso_ConflitoDeCodigo() {
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    CreateCursoDTO createCursoDTO = new CreateCursoDTO("Curso de Matemática", codigoCurso);

    Curso curso = Curso.builder()
      .name(createCursoDTO.name())
      .codigo(createCursoDTO.codigo())
      .build();

    when(cursoRepository.save(curso)).thenThrow(new DataIntegrityViolationException(""));

    Exception exception = assertThrows(ResourceConflictException.class, () -> {
      cursoService.createCurso(createCursoDTO);
    });

    assertEquals("Outro curso já está cadastrado com esse código: MT106TIN1", exception.getMessage());
    verify(cursoRepository, times(1)).save(curso);
  }

  @Test
  void createCurso_ErroInesperado() {
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    CreateCursoDTO createCursoDTO = new CreateCursoDTO("Curso de Matemática", codigoCurso);

    Curso curso = Curso.builder()
      .name(createCursoDTO.name())
      .codigo(createCursoDTO.codigo())
      .build();

    when(cursoRepository.save(curso)).thenThrow(new RuntimeException("Unexpected Error"));

    Exception exception = assertThrows(RuntimeException.class, () -> {
      cursoService.createCurso(createCursoDTO);
    });

    assertEquals("Erro inesperado ao criar curso.", exception.getMessage());
    verify(cursoRepository, times(1)).save(curso);
  }

  @Test
  void listAllCursos_Sucesso() {
    CodigoCurso codigoCurso1 = new CodigoCurso("MT106TIN1");
    CodigoCurso codigoCurso2 = new CodigoCurso("PH107TIN2");
    Curso curso1 = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso1)
      .build();
    Curso curso2 = Curso.builder()
      .id("e8d3837d-3ae2-498c-992c-d9af7d951a38")
      .name("Curso de Física")
      .codigo(codigoCurso2)
      .build();

    when(cursoRepository.findAll()).thenReturn(List.of(curso1, curso2));

    List<CursoSemMatriculasDTO> result = cursoService.listAllCursos();

    assertEquals(2, result.size());
    assertEquals("Curso de Matemática", result.get(0).name());
    assertEquals("PH107TIN2", result.get(1).codigo());
    verify(cursoRepository, times(1)).findAll();
  }

  @Test
  void getCurso_Sucesso() {
    CodigoCurso codigoCurso = new CodigoCurso("MT106TIN1");
    Curso curso = Curso.builder()
      .id("4f8ae9ff-b9f0-496d-8093-cee5edec5489")
      .name("Curso de Matemática")
      .codigo(codigoCurso)
      .matriculas(new ArrayList<>())
      .build();

    when(cursoRepository.findById("4f8ae9ff-b9f0-496d-8093-cee5edec5489")).thenReturn(Optional.of(curso));

    CursoComMatriculasDTO result = cursoService.getCurso("4f8ae9ff-b9f0-496d-8093-cee5edec5489");

    assertEquals("Curso de Matemática", result.name());
    assertEquals("MT106TIN1", result.codigo());
    verify(cursoRepository, times(1)).findById("4f8ae9ff-b9f0-496d-8093-cee5edec5489");
  }

  @Test
  void getCurso_NaoEncontrado() {
    when(cursoRepository.findById("0")).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      cursoService.getCurso("0");
    });

    assertEquals("Curso não encontrado com o id: 0", exception.getMessage());
    verify(cursoRepository, times(1)).findById("0");
  }
}
