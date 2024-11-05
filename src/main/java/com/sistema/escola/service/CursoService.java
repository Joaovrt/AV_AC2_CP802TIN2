package com.sistema.escola.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sistema.escola.dto.curso.CreateCursoDTO;
import com.sistema.escola.dto.curso.CursoComMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.entity.curso.Curso;
import com.sistema.escola.exceptions.ResourceConflictException;
import com.sistema.escola.exceptions.ResourceNotFoundException;
import com.sistema.escola.repository.CursoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {
  @Autowired
  private CursoRepository cursoRepository;

  public CursoSemMatriculasDTO createCurso(CreateCursoDTO createCursoDTO){
    try{
      Curso curso = Curso.builder()
      .name(createCursoDTO.name())
      .codigo(createCursoDTO.codigo())
      .build();
  
      var newCurso = cursoRepository.save(curso);
      return CursoSemMatriculasDTO.from(newCurso);
    }
    catch (DataIntegrityViolationException e) {
        throw new ResourceConflictException("Outro curso já está cadastrado com esse código: " + createCursoDTO.codigo().getCodigo());
    } 
    catch (Exception e) {
        throw new RuntimeException("Erro inesperado ao criar curso.");
    }
  }

  public List<CursoSemMatriculasDTO> listAllCursos(){

    var cursos = cursoRepository.findAll();

    return cursos.stream()
      .map(CursoSemMatriculasDTO::from)
      .collect(Collectors.toList());
  }

  public CursoComMatriculasDTO getCurso(String id){
    var curso = cursoRepository.findById(id);
    if(curso.isPresent())
      return CursoComMatriculasDTO.from(curso.get());
    else
      throw new ResourceNotFoundException("Curso não encontrado com o id: " + id);
  }
}
