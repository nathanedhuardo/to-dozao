package br.edu.ufape.todozao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "habit_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HabitHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @Column(name = "is_completed")
    private boolean completed;

    @Column(name = "created_at")
    private String createdAt;

    // RELACIONAMENTO COM TASK

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
