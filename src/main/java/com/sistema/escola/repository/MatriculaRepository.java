package com.sistema.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.escola.entity.matricula.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, String>{

}
