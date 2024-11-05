package com.sistema.escola.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.escola.dto.matricula.CreateMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaDTO;
import com.sistema.escola.dto.matricula.FinalizarMatriculaResponseDTO;
import com.sistema.escola.dto.matricula.MatriculaResponseDTO;
import com.sistema.escola.service.MatriculaService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/matricula")
public class MatriculaController {
  @Autowired
  private MatriculaService matriculaService;

  @PostMapping()
  public ResponseEntity<MatriculaResponseDTO> createMatricula(@RequestBody CreateMatriculaDTO createMatriculaDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.createMatricula(createMatriculaDTO));
  }

  @GetMapping()
  public ResponseEntity<List<MatriculaResponseDTO>> listAllMatriculas() {
      return ResponseEntity.status(HttpStatus.OK).body(matriculaService.listAllMatriculas());
  }

  @GetMapping(value="/{id}")
  public ResponseEntity<MatriculaResponseDTO> getMatricula(@PathVariable(value = "id") String id) {
      return ResponseEntity.status(HttpStatus.OK).body(matriculaService.getMatricula(id));
  }

  @GetMapping(value="iniciar/{id}")
  public ResponseEntity<MatriculaResponseDTO> iniciarMatricula(@PathVariable(value = "id") String id) {
      return ResponseEntity.status(HttpStatus.OK).body(matriculaService.iniciarMatricula(id));
  }

  @GetMapping(value="desistir/{id}")
  public ResponseEntity<MatriculaResponseDTO> desistirMatricula(@PathVariable(value = "id") String id) {
      return ResponseEntity.status(HttpStatus.OK).body(matriculaService.desistirMatricula(id));
  }
  
  @GetMapping(value="finalizar/{id}")
  public ResponseEntity<FinalizarMatriculaResponseDTO> finalizarMatricula(@PathVariable(value = "id") String id, @RequestBody FinalizarMatriculaDTO finalizarMatriculaDTO) {
      return ResponseEntity.status(HttpStatus.OK).body(matriculaService.finalizarMatricula(id,finalizarMatriculaDTO));
  }
}
