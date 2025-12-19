package br.edu.ufape.todozao.dto;

import lombok.Data;
import java.util.List;

@Data
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private List<TaskHistoryResponseDTO> history;
}
