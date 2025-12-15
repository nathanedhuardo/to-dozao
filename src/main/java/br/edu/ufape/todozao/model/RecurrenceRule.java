package br.edu.ufape.todozao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "recurrence_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurrenceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O tipo de recorrência é obrigatório")
    @Column(name = "recurrence_type")
    private String recurrenceType;



    @Min(value = 1, message = "O intervalo deve ser maior que zero")
    @NotNull(message = "O intervalo é obrigatório")
    @Column(name = "interval_value")
    private Integer interval;


    @Column(name = "end_date")
    private String endDate;

    @Column(name = "created_at")
    private String createdAt;

    @NotNull(message = "A tarefa é obrigatória")
    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
