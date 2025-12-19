package br.edu.ufape.todozao.service;

import br.edu.ufape.todozao.dto.ProjectCreateDTO;
import br.edu.ufape.todozao.exception.ProjectDuplicateException;
import br.edu.ufape.todozao.exception.UserNotFoundException;
import br.edu.ufape.todozao.model.Project;
import br.edu.ufape.todozao.model.User;
import br.edu.ufape.todozao.repository.ProjectRepository;
import br.edu.ufape.todozao.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project criarProjeto(ProjectCreateDTO dto) {

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        if (projectRepository.existsByNameAndUserId(dto.getName(), user.getId())) {
            throw new ProjectDuplicateException("Projeto já existe para esse usuário: " + dto.getName());
        }

        Project project = Project.builder()
                .name(dto.getName())
                .color(dto.getColor())
                .user(user)
                .build();

        return projectRepository.save(project);
    }

    public List<Project> listarProjetosDoUsuario(Long userId) {
        return projectRepository.findByUserId(userId);
    }
}
