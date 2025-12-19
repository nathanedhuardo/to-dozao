package br.edu.ufape.todozao.dto;

import br.edu.ufape.todozao.model.TaskStatus;
import lombok.Data;

@Data
public class TaskHistoryResponseDTO {

    private Long id;
    private TaskStatus status;
    private String notes;
    private String changedAt;
}
