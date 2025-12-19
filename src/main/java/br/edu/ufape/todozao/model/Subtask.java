package br.edu.ufape.todozao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "subtasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(name = "is_completed")
    private boolean completed;

    @Column(name = "created_at")
    private String createdAt;

    // RELACIONAMENTO COM TASK

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
