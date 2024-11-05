package com.sistema.escola.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sistema.escola.dto.aluno.AlunoComMatriculasDTO;
import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.aluno.CreateAlunoDTO;
import com.sistema.escola.entity.aluno.Aluno;
import com.sistema.escola.exceptions.ResourceConflictException;
import com.sistema.escola.exceptions.ResourceNotFoundException;
import com.sistema.escola.repository.AlunoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {

  @Autowired
  private AlunoRepository alunoRepository;

  public AlunoSemMatriculasDTO createAluno(CreateAlunoDTO createAlunoDTO){
    try{
      Aluno aluno = Aluno.builder()
      .name(createAlunoDTO.name())
      .email(createAlunoDTO.email())
      .qtdCursosDisponiveis(1)
      .build();
  
      var newAluno = alunoRepository.save(aluno);
      return AlunoSemMatriculasDTO.from(newAluno);
    }
    catch (DataIntegrityViolationException e) {
        throw new ResourceConflictException("Outro aluno já está cadastrado com esse e-mail: " + createAlunoDTO.email().getEmailAddress());
    } 
    catch (Exception e) {
        throw new RuntimeException("Erro inesperado ao criar aluno.");
    }
  }

  public List<AlunoSemMatriculasDTO> listAllAlunos(){

    var alunos = alunoRepository.findAll();

    return alunos.stream()
      .map(AlunoSemMatriculasDTO::from)
      .collect(Collectors.toList());
  }

  public AlunoComMatriculasDTO getAluno(String id){
    var aluno = alunoRepository.findById(id);
    if(aluno.isPresent())
      return AlunoComMatriculasDTO.from(aluno.get());
    else
      throw new ResourceNotFoundException("Aluno não encontrado com o id: " + id);
  }

}
