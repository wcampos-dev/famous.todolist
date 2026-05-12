package dev.wcampos.famous.todolist.service;


import dev.wcampos.famous.todolist.model.Status;
import dev.wcampos.famous.todolist.model.Task;
import dev.wcampos.famous.todolist.repository.TaskRepository;
import dev.wcampos.famous.todolist.util.TestUtils;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    // **Save**

    @Test
    void shouldSaveTaskWithValidDescription() {
        // **Arrange**
        // Task to test TaskService.saveTask()
        Task task = TestUtils.sampleTask(null, "My New Task", Status.PENDING);
        // Mock Task to be returned by repository
        Task savedTask = TestUtils.sampleTask(1L, "My second task", Status.PENDING);

        //Behavior
        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        // **Act

        Task result = taskService.saveTask(task);

        // **Assert**
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("My second task");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        Task task = TestUtils.sampleTask(null, "  ", Status.PENDING);

        assertThatThrownBy(() -> taskService.saveTask(task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task description cannot be empty or null.");
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsToLong() {
        String longDescription = "a.".repeat(201);
        Task task = TestUtils.sampleTask(null, longDescription, Status.PENDING);

        assertThatThrownBy(() -> taskService.saveTask(task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("grather than 200 characters");
    }

    @Test
    void shouldFindTaskById(){
        Task task = TestUtils.sampleTask(1L, "My new Task", Status.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.findById(1L);

        assertThat(result.getDescription()).isEqualTo("My new Task");
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Task not Found");
    }

    // **Update**

    @Test
    void shouldUpdateTaskStatus(){
        Task task = TestUtils.sampleTask(1L, "My New Task", Status.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class)))
                .thenReturn(TestUtils.sampleTask(1L, "My New Task", Status.DOING));

        Task update = taskService.updateStatus(1L, Status.DOING);

        assertThat(update.getStatus()).isEqualTo(Status.DOING);
    }

    // **Delete**

    @Test
    void shouldDeleteTaskSuccessfully() {
        Task task = TestUtils.sampleTask(1L, "My New Task", Status.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        boolean result = taskService.deleteTask(1L);

        assertThat(result).isTrue();
        verify(taskRepository).deleteById(1L);

    }

    @Test
    void ShouldReturnFalseWhenTaskNotFountForDeletion() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = taskService.deleteTask(99L);

        assertThat(result).isFalse();
        verify(taskRepository, never()).deleteById(anyLong());

    }



}
