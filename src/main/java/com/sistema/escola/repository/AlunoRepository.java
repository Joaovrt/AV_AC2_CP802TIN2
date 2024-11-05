package com.sistema.escola.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.escola.entity.aluno.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, String>{

}
