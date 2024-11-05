package com.sistema.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.escola.entity.curso.Curso;

public interface CursoRepository extends JpaRepository<Curso, String>{

}
