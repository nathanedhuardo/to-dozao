package br.edu.ufape.todozao.service;

import br.edu.ufape.todozao.dto.TaskCreateDTO;
import br.edu.ufape.todozao.exception.*;
import br.edu.ufape.todozao.model.Project;
import br.edu.ufape.todozao.model.Task;
import br.edu.ufape.todozao.model.TaskStatus;
import br.edu.ufape.todozao.model.User;
import br.edu.ufape.todozao.repository.ProjectRepository;
import br.edu.ufape.todozao.repository.TaskRepository;
import br.edu.ufape.todozao.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // ✅ CASO DE USO
    public Task criarTask(TaskCreateDTO dto) {

        // 1️⃣ validar invariantes cedo
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        // Converter String para enum TaskStatus
        TaskStatus status;
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                status = TaskStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidTaskStatusException(dto.getStatus());
            }
        } else {
            status = TaskStatus.PENDING; // Status padrão
        }

        Project project = null;
        if (dto.getProjectId() != null) {
            project = projectRepository.findById(dto.getProjectId())
                    .orElseThrow(() -> new ProjectNotFoundException(dto.getProjectId()));
        }

        // 2️⃣ idempotência simples
        if (taskRepository.existsByTitleAndUserId(dto.getTitle(), user.getId())) {
            throw new TaskDuplicateException("Task duplicada para esse usuário: " + dto.getTitle());
        }

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .color(dto.getColor())
                .priority(dto.getPriority())
                .user(user)
                .project(project)
                .taskStatus(status)
                .build();

        return taskRepository.save(task);
    }
}
