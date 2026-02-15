package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import org.zeta.model.Task;
import org.zeta.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public class TaskDao extends BaseDao<Task> {

    public TaskDao() {
        super("tasks.json", new TypeReference<List<Task>>() {});
    }


    public void saveTask(Task task) {
        add(task);
    }

    // Delete task
    public void deleteTask(Task task) {
        remove(task);
    }

    // Find task by ID
    public Optional<Task> findById(String id) {
        return dataList.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    // Find all tasks of a project
    public List<Task> findByProjectId(String projectId) {
        return dataList.stream()
                .filter(t -> t.getProjectId().equals(projectId))
                .toList();
    }

    // Find all tasks assigned to a builder
    public List<Task> findByBuilderId(Integer builderId) {
        return dataList.stream()
                .filter(t -> t.getBuilderId() != null && t.getBuilderId().equals(builderId))
                .toList();
    }

    // Assign builder to a task
    public void assignBuilder(String taskId, String builderId) {
        findById(taskId).ifPresent(task -> {
            task.setBuilderId(builderId);
            update(task);
        });
    }

    // Update task status
    public void updateStatus(String taskId, TaskStatus status) {
        findById(taskId).ifPresent(task -> {
            task.setStatus(status);
            update(task);
        });
    }

    // Update full task (like task name, builder, status)
    public void update(Task updatedTask) {
        findById(updatedTask.getId()).ifPresent(existing -> {
            dataList.remove(existing);
            dataList.add(updatedTask);
            saveToFile();
        });
    }
}
