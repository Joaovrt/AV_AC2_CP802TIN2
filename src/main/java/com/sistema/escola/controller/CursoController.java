package com.sistema.escola.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.escola.dto.curso.CreateCursoDTO;
import com.sistema.escola.dto.curso.CursoComMatriculasDTO;
import com.sistema.escola.dto.curso.CursoSemMatriculasDTO;
import com.sistema.escola.service.CursoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/curso")
public class CursoController {
  @Autowired
  private CursoService cursoService;

  @PostMapping()
  public ResponseEntity<CursoSemMatriculasDTO> createCurso(@RequestBody @Valid CreateCursoDTO createCursoDTO) {
      return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.createCurso(createCursoDTO));
  }
  
  @GetMapping()
  public ResponseEntity<List<CursoSemMatriculasDTO>> listAllCursos() {
      return ResponseEntity.status(HttpStatus.OK).body(cursoService.listAllCursos());
  }

  @GetMapping(value="/{id}")
  public ResponseEntity<CursoComMatriculasDTO> getCurso(@PathVariable(value = "id") String id) {
      return ResponseEntity.status(HttpStatus.OK).body(cursoService.getCurso(id));
  }
}
