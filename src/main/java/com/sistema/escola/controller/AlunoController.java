package com.sistema.escola.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.escola.dto.aluno.AlunoComMatriculasDTO;
import com.sistema.escola.dto.aluno.AlunoSemMatriculasDTO;
import com.sistema.escola.dto.aluno.CreateAlunoDTO;
import com.sistema.escola.service.AlunoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/aluno")
public class AlunoController {
  @Autowired
  private AlunoService alunoService;

  @PostMapping()
  public ResponseEntity<AlunoSemMatriculasDTO> createAluno(@RequestBody @Valid CreateAlunoDTO createAlunoDTO) {
      return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.createAluno(createAlunoDTO));
  }
  
  @GetMapping()
  public ResponseEntity<List<AlunoSemMatriculasDTO>> listAllAlunos() {
      return ResponseEntity.status(HttpStatus.OK).body(alunoService.listAllAlunos());
  }

  @GetMapping(value="/{id}")
  public ResponseEntity<AlunoComMatriculasDTO> getAluno(@PathVariable(value = "id") String id) {
      return ResponseEntity.status(HttpStatus.OK).body(alunoService.getAluno(id));
  }
}
