package br.edu.ufape.todozao.service;

import br.edu.ufape.todozao.model.Task;
import br.edu.ufape.todozao.model.TaskHistory;
import br.edu.ufape.todozao.model.TaskStatus;
import br.edu.ufape.todozao.repository.TaskHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskHistoryService {
    private final TaskHistoryRepository repository;

    public TaskHistoryService(TaskHistoryRepository repository){
        this.repository = repository;
    }

    public void register(Task task, TaskStatus newStatus, String notes){
        TaskHistory history = TaskHistory.builder()
                .task(task)
                .status(newStatus)
                .notes(notes)
                .changedAt(LocalDateTime.now().toString())
                .build();

        repository.save(history);
    }
}
