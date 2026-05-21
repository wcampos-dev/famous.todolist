package dev.wcampos.famous.todolist.repository;

import dev.wcampos.famous.todolist.model.Status;
import dev.wcampos.famous.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find by Status
    List<Task> findByStatus(Status status);

    // Personalized query
    // Find by description keyword
    @Query("SELECT t FROM Task t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%',:keyword, '%'))")
    List<Task> findByDescriptionContaining(@Param("keyword") String keyword);

}
