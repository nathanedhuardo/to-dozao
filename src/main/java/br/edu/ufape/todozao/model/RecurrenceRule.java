package br.edu.ufape.todozao.model;

import jakarta.persistence.*;
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

    @Column(name = "recurrence_type")
    private String recurrenceType; // diariamente, semanalmente ou mensalmente...

    private int interval;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "created_at")
    private String createdAt;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
