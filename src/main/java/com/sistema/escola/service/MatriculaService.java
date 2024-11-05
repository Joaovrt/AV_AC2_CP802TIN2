package com.sistema.escola.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sistema.escola.dto.matricula.CreateMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaResponseDTO;
import com.sistema.escola.dto.matricula.MatriculaResponseDTO;
import com.sistema.escola.entity.matricula.Matricula;
import com.sistema.escola.entity.matricula.StatusMatricula;
import com.sistema.escola.exceptions.ResourceConflictException;
import com.sistema.escola.exceptions.ResourceNotFoundException;
import com.sistema.escola.repository.AlunoRepository;
import com.sistema.escola.repository.CursoRepository;
import com.sistema.escola.repository.MatriculaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {
  @Autowired
  private MatriculaRepository matriculaRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private CursoRepository cursoRepository;

  public MatriculaResponseDTO createMatricula(CreateMatriculaDTO createMatriculaDTO){
    try{

      var aluno = alunoRepository.findById(createMatriculaDTO.aluno_id());
      if(aluno.isEmpty())
        throw new ResourceNotFoundException("Nenhum aluno encontrado com o id: " + createMatriculaDTO.aluno_id());
      if(aluno.get().getQtdCursosDisponiveis()<=0)
        throw new ResourceConflictException("Aluno não tem quantidade de cursos disponíveis.");
      aluno.get().setQtdCursosDisponiveis(aluno.get().getQtdCursosDisponiveis()-1);
      var newAluno = alunoRepository.save(aluno.get()); 

      var curso = cursoRepository.findById(createMatriculaDTO.curso_id());
      if(curso.isEmpty())
        throw new ResourceNotFoundException("Nenhum curso encontrado com o id: " + createMatriculaDTO.curso_id());

      Matricula matricula = Matricula.builder()
        .codigo(createMatriculaDTO.codigo())
        .media(0)
        .status(StatusMatricula.NAO_INICIADO)
        .aluno(newAluno)
        .curso(curso.get())
        .build();
  
      var newMatricula= matriculaRepository.save(matricula);
      return MatriculaResponseDTO.from(newMatricula);
    }
    catch (DataIntegrityViolationException e) {
        throw new ResourceConflictException("O aluno já está matriculado nesse curso.");
    }
    catch (Exception e) {
      if (e instanceof ResourceNotFoundException || e instanceof ResourceConflictException) {
        throw e;
      }
      throw new RuntimeException("Erro inesperado ao criar matricula.");
    }
  }

  public List<MatriculaResponseDTO> listAllMatriculas(){

    var matriculas = matriculaRepository.findAll();

    return matriculas.stream()
      .map(MatriculaResponseDTO::from)
      .collect(Collectors.toList());
  }

  public MatriculaResponseDTO getMatricula(String id){
    var matricula = matriculaRepository.findById(id);
    if(matricula.isPresent())
      return MatriculaResponseDTO.from(matricula.get());
    else
      throw new ResourceNotFoundException("Matricula não encontrada com o id: " + id);
  }

  public MatriculaResponseDTO iniciarMatricula(String id){
    var matricula = matriculaRepository.findById(id);
    if(matricula.isEmpty())
      throw new ResourceNotFoundException("Matricula não encontrada com o id: " + id);
    if(matricula.get().getStatus()!= StatusMatricula.NAO_INICIADO)
      throw new ResourceConflictException("Matricula deve estar como 'Não iniciado' para começar.");
    matricula.get().setStatus(StatusMatricula.EM_ANDAMENTO);
    var newMatricula = matriculaRepository.save(matricula.get());
    return MatriculaResponseDTO.from(newMatricula);
  }

  public MatriculaResponseDTO desistirMatricula(String id){
    var matricula = matriculaRepository.findById(id);
    if(matricula.isEmpty())
      throw new ResourceNotFoundException("Matricula não encontrada com o id: " + id);
    if(matricula.get().getStatus()!= StatusMatricula.EM_ANDAMENTO)
      throw new ResourceConflictException("Matricula deve estar como 'Em andamento' para desistir.");
    matricula.get().setStatus(StatusMatricula.NAO_INICIADO);
    var newMatricula = matriculaRepository.save(matricula.get());
    return MatriculaResponseDTO.from(newMatricula);
  }

  public FinalizarMatriculaResponseDTO finalizarMatricula(String id, FinalizarMatriculaDTO finalizarMatriculaDTO){
    var matricula = matriculaRepository.findById(id);
    if(matricula.isEmpty())
      throw new ResourceNotFoundException("Matricula não encontrada com o id: " + id);
    if(matricula.get().getStatus()!= StatusMatricula.EM_ANDAMENTO)
      throw new ResourceConflictException("Matricula deve estar como 'Em andamento' para finalizar.");
    matricula.get().setStatus(StatusMatricula.FINALIZADO);
    matricula.get().setMedia(finalizarMatriculaDTO.media());
    var newMatricula = matriculaRepository.save(matricula.get());
    var mensagem = "Nota mínima para o benefício não atingida. Assista ao vídeo listado na aba 'Revisão'.";
    if(finalizarMatriculaDTO.media()>7.0){
      var aluno = alunoRepository.findById(matricula.get().getAluno().getId());
      aluno.get().setQtdCursosDisponiveis(aluno.get().getQtdCursosDisponiveis()+3);
      alunoRepository.save(aluno.get()); 
      mensagem = "Benefício de mais 3 cursos adquirido!";
    }
    return FinalizarMatriculaResponseDTO.from(newMatricula,mensagem);
  }
}
