package br.edu.ufape.todozao.repository;

import br.edu.ufape.todozao.model.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {//solicita um repositório para Subtask e o tipo da chave primária é Long
    
    List<Subtask> findByTaskId(Long taskId);
    
    List<Subtask> findByTaskIdAndCompleted(Long taskId, boolean completed);



}
