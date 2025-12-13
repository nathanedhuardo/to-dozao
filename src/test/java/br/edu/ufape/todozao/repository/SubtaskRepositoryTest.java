package br.edu.ufape.todozao.repository;

import br.edu.ufape.todozao.model.Project;
import br.edu.ufape.todozao.model.Subtask;
import br.edu.ufape.todozao.model.Task;
import br.edu.ufape.todozao.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SubtaskRepositoryTest {

    @Autowired
    private SubtaskRepository subtaskRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .build();
        entityManager.persist(user);
        entityManager.flush();

        project = Project.builder()
                .name("Test Project")
                .color("#FF0000")
                .user(user)
                .build();
        entityManager.persist(project);
        entityManager.flush();

        task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .user(user)
                .project(project)
                .build();
        entityManager.persist(task);
        entityManager.flush();
    }

    @Test
    void testSaveSubtask() {
        Subtask subtask = Subtask.builder()
                .title("Subtask 1")
                .completed(false)
                .createdAt("2024-01-01")
                .task(task)
                .build();

        Subtask saved = subtaskRepository.save(subtask);

        assertNotNull(saved.getId());
        assertEquals("Subtask 1", saved.getTitle());
        assertFalse(saved.isCompleted());
        assertEquals(task.getId(), saved.getTask().getId());
    }

    @Test
    void testFindById() {
        Subtask subtask = Subtask.builder()
                .title("Subtask Test")
                .completed(false)
                .task(task)
                .build();
        Subtask saved = subtaskRepository.save(subtask);

        Optional<Subtask> found = subtaskRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Subtask Test", found.get().getTitle());
    }

    @Test
    void testFindByTaskId() {
        Subtask subtask1 = Subtask.builder()
                .title("Subtask 1")
                .completed(false)
                .task(task)
                .build();

        Subtask subtask2 = Subtask.builder()
                .title("Subtask 2")
                .completed(true)
                .task(task)
                .build();

        Task task2 = Task.builder()
                .title("Task 2")
                .user(user)
                .project(project)
                .build();
        entityManager.persist(task2);
        entityManager.flush();

        Subtask subtask3 = Subtask.builder()
                .title("Subtask 3")
                .completed(false)
                .task(task2)
                .build();

        subtaskRepository.save(subtask1);
        subtaskRepository.save(subtask2);
        subtaskRepository.save(subtask3);

        List<Subtask> subtasks = subtaskRepository.findByTaskId(task.getId());

        assertEquals(2, subtasks.size());
        assertTrue(subtasks.stream().anyMatch(s -> s.getTitle().equals("Subtask 1")));
        assertTrue(subtasks.stream().anyMatch(s -> s.getTitle().equals("Subtask 2")));
    }

    @Test
    void testFindByTaskIdAndCompleted() {
        Subtask subtask1 = Subtask.builder()
                .title("Subtask 1")
                .completed(false)
                .task(task)
                .build();

        Subtask subtask2 = Subtask.builder()
                .title("Subtask 2")
                .completed(true)
                .task(task)
                .build();

        Subtask subtask3 = Subtask.builder()
                .title("Subtask 3")
                .completed(false)
                .task(task)
                .build();

        subtaskRepository.save(subtask1);
        subtaskRepository.save(subtask2);
        subtaskRepository.save(subtask3);

        List<Subtask> completedSubtasks = subtaskRepository.findByTaskIdAndCompleted(task.getId(), true);
        List<Subtask> incompleteSubtasks = subtaskRepository.findByTaskIdAndCompleted(task.getId(), false);

        assertEquals(1, completedSubtasks.size());
        assertEquals("Subtask 2", completedSubtasks.get(0).getTitle());
        assertEquals(2, incompleteSubtasks.size());
    }

    @Test
    void testUpdateSubtask() {
        Subtask subtask = Subtask.builder()
                .title("Subtask Test")
                .completed(false)
                .task(task)
                .build();
        Subtask saved = subtaskRepository.save(subtask);

        saved.setTitle("Updated Subtask");
        saved.setCompleted(true);
        Subtask updated = subtaskRepository.save(saved);

        assertEquals("Updated Subtask", updated.getTitle());
        assertTrue(updated.isCompleted());
    }

    @Test
    void testDeleteSubtask() {
        Subtask subtask = Subtask.builder()
                .title("Subtask to Delete")
                .completed(false)
                .task(task)
                .build();
        Subtask saved = subtaskRepository.save(subtask);
        Long id = saved.getId();

        subtaskRepository.delete(saved);
        entityManager.flush();
        entityManager.clear();

        Optional<Subtask> found = subtaskRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        Subtask subtask1 = Subtask.builder()
                .title("Subtask 1")
                .completed(false)
                .task(task)
                .build();

        Subtask subtask2 = Subtask.builder()
                .title("Subtask 2")
                .completed(true)
                .task(task)
                .build();

        subtaskRepository.save(subtask1);
        subtaskRepository.save(subtask2);

        long count = subtaskRepository.count();

        assertEquals(2, count);
    }
}

