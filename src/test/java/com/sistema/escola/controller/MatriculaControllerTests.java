package com.sistema.escola.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.dto.matricula.CreateMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaResponseDTO;
import com.sistema.escola.dto.matricula.MatriculaResponseDTO;
import com.sistema.escola.entity.matricula.CodigoMatricula;
import com.sistema.escola.entity.matricula.StatusMatricula;
import com.sistema.escola.service.MatriculaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

@WebMvcTest(MatriculaController.class)
public class MatriculaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatriculaService matriculaService;

    @Test
    public void createMatricula_ReturnsCreatedStatus_WhenRequestIsValid() throws Exception {
        CodigoMatricula codigo = new CodigoMatricula("AA00AA00");
        AlunoSemMatriculasDTO alunoSemMatriculasDTO = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 1);
        CursoSemMatriculasDTO cursoSemMatriculasDTO = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");
        CreateMatriculaDTO createMatriculaDTO = new CreateMatriculaDTO(codigo,"IDALUNO","IDCURSO");
        MatriculaResponseDTO expectedResponse = new MatriculaResponseDTO("IDMATRICULA",0,StatusMatricula.NAO_INICIADO,"AA00AA00",alunoSemMatriculasDTO,cursoSemMatriculasDTO);

        when(matriculaService.createMatricula(createMatriculaDTO)).thenReturn(expectedResponse);

        mockMvc.perform(post("/matricula")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMatriculaDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo").value("AA00AA00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NAO_INICIADO"));
    }

    @Test
    public void listAllMatriculas_ReturnsOkStatus_WithListOfMatriculas() throws Exception {
        AlunoSemMatriculasDTO aluno = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 1);
        CursoSemMatriculasDTO curso = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");
        
        MatriculaResponseDTO matricula1 = new MatriculaResponseDTO("IDMATRICULA1", 0, StatusMatricula.NAO_INICIADO, "AA00AA00", aluno, curso);
        MatriculaResponseDTO matricula2 = new MatriculaResponseDTO("IDMATRICULA2", 0, StatusMatricula.EM_ANDAMENTO, "BB00BB00", aluno, curso);

        List<MatriculaResponseDTO> matriculas = List.of(matricula1, matricula2);

        when(matriculaService.listAllMatriculas()).thenReturn(matriculas);

        mockMvc.perform(get("/matricula")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(matriculas.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].codigo").value("AA00AA00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("NAO_INICIADO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].codigo").value("BB00BB00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value("EM_ANDAMENTO"));
    }

    @Test
    public void getMatricula_ReturnsOkStatus_WithMatricula() throws Exception {
        String matriculaId = "IDMATRICULA";
        AlunoSemMatriculasDTO aluno = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 1);
        CursoSemMatriculasDTO curso = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");

        MatriculaResponseDTO expectedResponse = new MatriculaResponseDTO(
                matriculaId, 0, StatusMatricula.NAO_INICIADO, "AA00AA00", aluno, curso
        );

        when(matriculaService.getMatricula(matriculaId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/matricula/{id}", matriculaId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo").value("AA00AA00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NAO_INICIADO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(matriculaId));
    }

    @Test
    public void iniciarMatricula_ReturnsOkStatus_WithUpdatedMatricula() throws Exception {
        String matriculaId = "IDMATRICULA";
        AlunoSemMatriculasDTO aluno = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 1);
        CursoSemMatriculasDTO curso = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");

        MatriculaResponseDTO updatedMatriculaResponse = new MatriculaResponseDTO(
                matriculaId, 0, StatusMatricula.EM_ANDAMENTO, "AA00AA00", aluno, curso
        );

        when(matriculaService.iniciarMatricula(matriculaId)).thenReturn(updatedMatriculaResponse);

        mockMvc.perform(post("/matricula/iniciar/{id}", matriculaId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("EM_ANDAMENTO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo").value("AA00AA00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(matriculaId));
    }

    @Test
    public void desistirMatricula_ReturnsOkStatus_WithUpdatedMatricula() throws Exception {
        String matriculaId = "IDMATRICULA";
        AlunoSemMatriculasDTO aluno = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 1);
        CursoSemMatriculasDTO curso = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");

        MatriculaResponseDTO updatedMatriculaResponse = new MatriculaResponseDTO(
                matriculaId, 0, StatusMatricula.NAO_INICIADO, "AA00AA00", aluno, curso
        );

        when(matriculaService.desistirMatricula(matriculaId)).thenReturn(updatedMatriculaResponse);

        mockMvc.perform(post("/matricula/desistir/{id}", matriculaId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NAO_INICIADO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.codigo").value("AA00AA00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(matriculaId));
    }

     @Test
    public void finalizarMatricula_ReturnsExpectedMessage_WhenMediaIs7() throws Exception {

        String matriculaId = "IDMATRICULA";
        FinalizarMatriculaDTO finalizarMatriculaDTO = new FinalizarMatriculaDTO(7.0);

        AlunoSemMatriculasDTO aluno = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 1);
        CursoSemMatriculasDTO curso = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");

        FinalizarMatriculaResponseDTO expectedResponse = new FinalizarMatriculaResponseDTO(
                matriculaId, 7.0, StatusMatricula.FINALIZADO, "AA00AA00", aluno, curso, 
                "Nota mínima para o benefício não atingida. Assista ao vídeo listado na aba 'Revisão'."
        );

        when(matriculaService.finalizarMatricula(matriculaId, finalizarMatriculaDTO)).thenReturn(expectedResponse);

        mockMvc.perform(post("/matricula/finalizar/{id}", matriculaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(finalizarMatriculaDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.media").value(7.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FINALIZADO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem").value(
                        "Nota mínima para o benefício não atingida. Assista ao vídeo listado na aba 'Revisão'."));
    }

    @Test
    public void finalizarMatricula_ReturnsExpectedMessage_WhenMediaIsAbove7() throws Exception {
        String matriculaId = "IDMATRICULA";
        FinalizarMatriculaDTO finalizarMatriculaDTO = new FinalizarMatriculaDTO(7.1);
        AlunoSemMatriculasDTO aluno = new AlunoSemMatriculasDTO("IDALUNO", "NOME", "joao@email.com", 4);
        CursoSemMatriculasDTO curso = new CursoSemMatriculasDTO("IDCURSO", "CURSO", "CP000AAA0");

        FinalizarMatriculaResponseDTO expectedResponse = new FinalizarMatriculaResponseDTO(
                matriculaId, 7.1, StatusMatricula.FINALIZADO, "AA00AA00", aluno, curso, 
                "Benefício de mais 3 cursos adquirido!"
        );

        when(matriculaService.finalizarMatricula(matriculaId, finalizarMatriculaDTO)).thenReturn(expectedResponse);

        mockMvc.perform(post("/matricula/finalizar/{id}", matriculaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(finalizarMatriculaDTO)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.media").value(7.1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("FINALIZADO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem").value("Benefício de mais 3 cursos adquirido!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.aluno.qtdCursosDisponiveis").value(4));
    }
}
