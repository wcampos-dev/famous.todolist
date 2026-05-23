package dev.wcampos.famous.todolist.repository;

import dev.wcampos.famous.todolist.model.Status;
import dev.wcampos.famous.todolist.model.Task;
import dev.wcampos.famous.todolist.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    // Currently testing only happy path here for now, more test MUST be added later

    @Test
    void shouldFindByStatus() {
        // Arrange

        //Create mock test
        Task task = TestUtils.sampleTask(null, "Task to be saved", Status.PENDING);
        taskRepository.save(task);

        // Act
        List<Task> pendingTasks = taskRepository.findByStatus(Status.PENDING);

        // Assert
        assertThat(pendingTasks).hasSize(7);
        assertThat(pendingTasks.get(0).getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void shouldFindByDescriptionContaining() {
        Task task = TestUtils.sampleTask(null, "My Task containing Test keyword", Status.PENDING);
        taskRepository.save(task);

        List<Task> keywordList = taskRepository.findByDescriptionContaining("TesT");

        assertThat(keywordList).hasSize(3);
        assertThat(keywordList.get(0).getDescription()).contains("Update");
    }

}
