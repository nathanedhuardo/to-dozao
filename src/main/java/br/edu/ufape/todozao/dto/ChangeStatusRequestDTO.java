package br.edu.ufape.todozao.dto;
import br.edu.ufape.todozao.model.TaskStatus;
import lombok.Data;

@Data
public class ChangeStatusRequestDTO {

    private TaskStatus newStatus;
    private String notes;
}
