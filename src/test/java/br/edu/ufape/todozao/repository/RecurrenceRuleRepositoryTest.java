package br.edu.ufape.todozao.repository;

import br.edu.ufape.todozao.model.Project;
import br.edu.ufape.todozao.model.RecurrenceRule;
import br.edu.ufape.todozao.model.Task;
import br.edu.ufape.todozao.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecurrenceRuleRepositoryTest {

    @Autowired
    private RecurrenceRuleRepository recurrenceRuleRepository;

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
    void testSaveRecurrenceRule() {
        RecurrenceRule rule = RecurrenceRule.builder()
                .recurrenceType("diariamente")
                .interval(1)
                .endDate("2024-12-31")
                .createdAt("2024-01-01")
                .task(task)
                .build();

        RecurrenceRule saved = recurrenceRuleRepository.save(rule);

        assertNotNull(saved.getId());
        assertEquals("diariamente", saved.getRecurrenceType());
        assertEquals(1, saved.getInterval());
        assertEquals(task.getId(), saved.getTask().getId());
    }

    @Test
    void testFindById() {
        RecurrenceRule rule = RecurrenceRule.builder()
                .recurrenceType("semanalmente")
                .interval(1)
                .task(task)
                .build();
        RecurrenceRule saved = recurrenceRuleRepository.save(rule);

        Optional<RecurrenceRule> found = recurrenceRuleRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("semanalmente", found.get().getRecurrenceType());
    }

    @Test
    void testUpdateRecurrenceRule() {
        RecurrenceRule rule = RecurrenceRule.builder()
                .recurrenceType("mensalmente")
                .interval(1)
                .task(task)
                .build();
        RecurrenceRule saved = recurrenceRuleRepository.save(rule);

        saved.setRecurrenceType("anualmente");
        saved.setInterval(12);
        RecurrenceRule updated = recurrenceRuleRepository.save(saved);

        assertEquals("anualmente", updated.getRecurrenceType());
        assertEquals(12, updated.getInterval());
    }

    @Test
    void testDeleteRecurrenceRule() {
        RecurrenceRule rule = RecurrenceRule.builder()
                .recurrenceType("diariamente")
                .interval(1)
                .task(task)
                .build();
        RecurrenceRule saved = recurrenceRuleRepository.save(rule);
        Long id = saved.getId();

        recurrenceRuleRepository.delete(saved);
        entityManager.flush();
        entityManager.clear();

        Optional<RecurrenceRule> found = recurrenceRuleRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        RecurrenceRule rule1 = RecurrenceRule.builder()
                .recurrenceType("diariamente")
                .interval(1)
                .task(task)
                .build();

        Task task2 = Task.builder()
                .title("Task 2")
                .user(user)
                .project(project)
                .build();
        entityManager.persist(task2);
        entityManager.flush();

        RecurrenceRule rule2 = RecurrenceRule.builder()
                .recurrenceType("semanalmente")
                .interval(1)
                .task(task2)
                .build();

        recurrenceRuleRepository.save(rule1);
        recurrenceRuleRepository.save(rule2);

        long count = recurrenceRuleRepository.count();

        assertEquals(2, count);
    }

    @Test
    void testFindByTaskId() {
        RecurrenceRule rule = RecurrenceRule.builder()
                .recurrenceType("diariamente")
                .interval(1)
                .task(task)
                .build();
        recurrenceRuleRepository.save(rule);

        Task task2 = Task.builder()
                .title("Task 2")
                .user(user)
                .project(project)
                .build();
        entityManager.persist(task2);
        entityManager.flush();

        RecurrenceRule rule2 = RecurrenceRule.builder()
                .recurrenceType("semanalmente")
                .interval(1)
                .task(task2)
                .build();
        recurrenceRuleRepository.save(rule2);

        Optional<RecurrenceRule> found = recurrenceRuleRepository.findByTaskId(task.getId());

        assertTrue(found.isPresent());
        assertEquals("diariamente", found.get().getRecurrenceType());
        assertEquals(task.getId(), found.get().getTask().getId());
    }
}

