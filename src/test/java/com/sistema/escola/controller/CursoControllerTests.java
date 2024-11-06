package com.sistema.escola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.escola.dto.curso.CursoComMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.service.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CursoController.class)
public class CursoControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CursoService cursoService;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String CURSO_ID = "1";
  private static final String CURSO_NAME = "Curso de Java";
  private static final String CURSO_CODIGO = "JA123JAV1";

  @Test
  public void testCreateCurso() throws Exception {
    CursoSemMatriculasDTO cursoSemMatriculasDTO = new CursoSemMatriculasDTO(CURSO_ID, CURSO_NAME, CURSO_CODIGO);

    when(cursoService.createCurso(any())).thenReturn(cursoSemMatriculasDTO);

    mockMvc.perform(post("/curso")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cursoSemMatriculasDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(CURSO_ID))
            .andExpect(jsonPath("$.name").value(CURSO_NAME))
            .andExpect(jsonPath("$.codigo").value(CURSO_CODIGO));

    verify(cursoService, times(1)).createCurso(any());
  }

  @Test
  public void testListAllCursos() throws Exception {
    CursoSemMatriculasDTO curso1 = new CursoSemMatriculasDTO(CURSO_ID, CURSO_NAME, CURSO_CODIGO);
    List<CursoSemMatriculasDTO> cursos = List.of(curso1);

    when(cursoService.listAllCursos()).thenReturn(cursos);

    mockMvc.perform(get("/curso"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(CURSO_ID))
            .andExpect(jsonPath("$[0].name").value(CURSO_NAME))
            .andExpect(jsonPath("$[0].codigo").value(CURSO_CODIGO));

    verify(cursoService, times(1)).listAllCursos();
  }

  @Test
  public void testGetCurso() throws Exception {
    CursoComMatriculasDTO cursoComMatriculasDTO = new CursoComMatriculasDTO(CURSO_ID, CURSO_NAME, CURSO_CODIGO, List.of());

    when(cursoService.getCurso(CURSO_ID)).thenReturn(cursoComMatriculasDTO);

    mockMvc.perform(get("/curso/{id}", CURSO_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(CURSO_ID))
            .andExpect(jsonPath("$.name").value(CURSO_NAME))
            .andExpect(jsonPath("$.codigo").value(CURSO_CODIGO));

    verify(cursoService, times(1)).getCurso(CURSO_ID);
  }
}
