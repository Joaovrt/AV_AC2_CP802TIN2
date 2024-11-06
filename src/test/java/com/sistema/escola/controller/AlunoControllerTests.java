package com.sistema.escola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.escola.dto.aluno.AlunoComMatriculasDTO;
import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.aluno.CreateAlunoDTO;
import com.sistema.escola.entity.aluno.Email;
import com.sistema.escola.service.AlunoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
public class AlunoControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AlunoService alunoService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void createAluno_ShouldReturnCreated() throws Exception {
    Email email = new Email("joao.silva@email.com");
    CreateAlunoDTO createAlunoDTO = new CreateAlunoDTO("João Silva", email);
    AlunoSemMatriculasDTO alunoResponse = new AlunoSemMatriculasDTO("1", "João Silva", "joao.silva@email.com", 1);

    Mockito.when(alunoService.createAluno(any(CreateAlunoDTO.class))).thenReturn(alunoResponse);

    ResultActions result = mockMvc.perform(post("/aluno")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createAlunoDTO)));

    result.andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value("1"))
          .andExpect(jsonPath("$.name").value("João Silva"))
          .andExpect(jsonPath("$.email").value("joao.silva@email.com"));
  }

  @Test
  public void listAllAlunos_ShouldReturnListOfAlunos() throws Exception {
    List<AlunoSemMatriculasDTO> alunos = List.of(
            new AlunoSemMatriculasDTO("1", "João Silva", "joao.silva@email.com",1),
            new AlunoSemMatriculasDTO("2", "Maria Souza", "maria.souza@email.com",1)
    );

    Mockito.when(alunoService.listAllAlunos()).thenReturn(alunos);

    ResultActions result = mockMvc.perform(get("/aluno")
            .contentType(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk())
          .andExpect(jsonPath("$.size()").value(alunos.size()))
          .andExpect(jsonPath("$[0].name").value("João Silva"))
          .andExpect(jsonPath("$[1].name").value("Maria Souza"));
  }

  @Test
  public void getAluno_ShouldReturnAlunoWithMatriculas() throws Exception {
    String alunoId = "1";
    AlunoComMatriculasDTO aluno = new AlunoComMatriculasDTO(alunoId, "João Silva", "joao.silva@email.com", 1, List.of());

    Mockito.when(alunoService.getAluno(anyString())).thenReturn(aluno);

    ResultActions result = mockMvc.perform(get("/aluno/{id}", alunoId)
            .contentType(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(alunoId))
          .andExpect(jsonPath("$.name").value("João Silva"))
          .andExpect(jsonPath("$.email").value("joao.silva@email.com"));
  }
}
