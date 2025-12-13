package br.edu.ufape.todozao.repository;

import br.edu.ufape.todozao.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {//solicita um repositório para TaskHistory e o tipo da chave primária é Long

}
