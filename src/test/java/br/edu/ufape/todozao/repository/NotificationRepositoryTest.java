package br.edu.ufape.todozao.repository;

import br.edu.ufape.todozao.model.Notification;
import br.edu.ufape.todozao.model.Project;
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
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

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
    void testSaveNotification() {
        Notification notification = new Notification();
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test notification");
        notification.setRead(false);
        notification.setTask(task);

        Notification saved = notificationRepository.save(notification);

        assertNotNull(saved.getId());
        assertEquals("Test Notification", saved.getTitle());
        assertEquals("This is a test notification", saved.getMessage());
        assertFalse(saved.isRead());
        assertNotNull(saved.getCreatedAt());
        assertEquals(task.getId(), saved.getTask().getId());
    }

    @Test
    void testFindById() {
        Notification notification = new Notification();
        notification.setTitle("Test Notification");
        notification.setMessage("Test Message");
        notification.setRead(false);
        notification.setTask(task);
        Notification saved = notificationRepository.save(notification);

        Optional<Notification> found = notificationRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Notification", found.get().getTitle());
        assertEquals("Test Message", found.get().getMessage());
    }

    @Test
    void testFindByTaskId() {
        Notification notification1 = new Notification();
        notification1.setTitle("Notification 1");
        notification1.setMessage("Message 1");
        notification1.setRead(false);
        notification1.setTask(task);

        Notification notification2 = new Notification();
        notification2.setTitle("Notification 2");
        notification2.setMessage("Message 2");
        notification2.setRead(true);
        notification2.setTask(task);

        Task task2 = Task.builder()
                .title("Task 2")
                .user(user)
                .project(project)
                .build();
        entityManager.persist(task2);
        entityManager.flush();

        Notification notification3 = new Notification();
        notification3.setTitle("Notification 3");
        notification3.setMessage("Message 3");
        notification3.setRead(false);
        notification3.setTask(task2);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        List<Notification> notifications = notificationRepository.findByTaskId(task.getId());

        assertEquals(2, notifications.size());
        assertTrue(notifications.stream().anyMatch(n -> n.getTitle().equals("Notification 1")));
        assertTrue(notifications.stream().anyMatch(n -> n.getTitle().equals("Notification 2")));
    }

    @Test
    void testFindByRead() {
        Notification notification1 = new Notification();
        notification1.setTitle("Unread Notification");
        notification1.setMessage("Message 1");
        notification1.setRead(false);
        notification1.setTask(task);

        Notification notification2 = new Notification();
        notification2.setTitle("Read Notification");
        notification2.setMessage("Message 2");
        notification2.setRead(true);
        notification2.setTask(task);

        Notification notification3 = new Notification();
        notification3.setTitle("Another Unread");
        notification3.setMessage("Message 3");
        notification3.setRead(false);
        notification3.setTask(task);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        List<Notification> readNotifications = notificationRepository.findByRead(true);
        List<Notification> unreadNotifications = notificationRepository.findByRead(false);

        assertEquals(1, readNotifications.size());
        assertEquals("Read Notification", readNotifications.get(0).getTitle());
        assertEquals(2, unreadNotifications.size());
    }

    @Test
    void testFindByTaskIdAndRead() {
        Notification notification1 = new Notification();
        notification1.setTitle("Unread Notification");
        notification1.setMessage("Message 1");
        notification1.setRead(false);
        notification1.setTask(task);

        Notification notification2 = new Notification();
        notification2.setTitle("Read Notification");
        notification2.setMessage("Message 2");
        notification2.setRead(true);
        notification2.setTask(task);

        Notification notification3 = new Notification();
        notification3.setTitle("Another Unread");
        notification3.setMessage("Message 3");
        notification3.setRead(false);
        notification3.setTask(task);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        List<Notification> unreadForTask = notificationRepository.findByTaskIdAndRead(task.getId(), false);
        List<Notification> readForTask = notificationRepository.findByTaskIdAndRead(task.getId(), true);

        assertEquals(2, unreadForTask.size());
        assertEquals(1, readForTask.size());
        assertEquals("Read Notification", readForTask.get(0).getTitle());
    }

    @Test
    void testUpdateNotification() {
        Notification notification = new Notification();
        notification.setTitle("Original Title");
        notification.setMessage("Original Message");
        notification.setRead(false);
        notification.setTask(task);
        Notification saved = notificationRepository.save(notification);

        saved.setTitle("Updated Title");
        saved.setMessage("Updated Message");
        saved.setRead(true);
        Notification updated = notificationRepository.save(saved);

        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated Message", updated.getMessage());
        assertTrue(updated.isRead());
    }

    @Test
    void testDeleteNotification() {
        Notification notification = new Notification();
        notification.setTitle("Notification to Delete");
        notification.setMessage("This will be deleted");
        notification.setRead(false);
        notification.setTask(task);
        Notification saved = notificationRepository.save(notification);
        Long id = saved.getId();

        notificationRepository.delete(saved);
        entityManager.flush();
        entityManager.clear();

        Optional<Notification> found = notificationRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        Notification notification1 = new Notification();
        notification1.setTitle("Notification 1");
        notification1.setMessage("Message 1");
        notification1.setRead(false);
        notification1.setTask(task);

        Notification notification2 = new Notification();
        notification2.setTitle("Notification 2");
        notification2.setMessage("Message 2");
        notification2.setRead(true);
        notification2.setTask(task);

        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        long count = notificationRepository.count();

        assertEquals(2, count);
    }

    @Test
    void testCreatedAtIsAutomaticallySet() {
        Notification notification = new Notification();
        notification.setTitle("Test Notification");
        notification.setMessage("Test Message");
        notification.setRead(false);
        notification.setTask(task);

        Notification saved = notificationRepository.save(notification);

        assertNotNull(saved.getCreatedAt());
    }
}

