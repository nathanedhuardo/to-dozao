package br.edu.ufape.todozao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateDTO {

    @NotBlank
    private String title;

    private String description;
    private String color;
    private String priority;

    @NotNull
    private Long userId;

    private Long projectId;

    private String status;
}
