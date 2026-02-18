package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import org.zeta.model.Task;
import org.zeta.model.enums.TaskStatus;

import java.util.List;
import java.util.Optional;

public class TaskDao extends BaseDao<Task> {

    public TaskDao() {
        super("tasks.json", new TypeReference<List<Task>>() {});
    }


    public void saveTask(Task task) {
        add(task);
    }

    public void deleteTask(Task task) {
        remove(task);
    }

    public  Optional<Task> findById(String id) {
        return dataList.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public List<Task> findByProjectId(String projectId) {
        return dataList.stream()
                .filter(t -> t.getProjectId().equals(projectId))
                .toList();
    }

    public List<Task> findByBuilderId(Integer builderId) {
        return dataList.stream()
                .filter(t -> t.getBuilderId() != null && t.getBuilderId().equals(builderId))
                .toList();
    }


    public void assignBuilder(String taskId, String builderId) {
        findById(taskId).ifPresent(task -> {
            task.setBuilderId(builderId);
            update(task);
        });
    }

    public void updateStatus(String taskId, TaskStatus status) {
        findById(taskId).ifPresent(task -> {
            task.setStatus(status);
            update(task);
        });
    }

    public void update(Task updatedTask) {
        findById(updatedTask.getId()).ifPresent(existing -> {
            dataList.remove(existing);
            dataList.add(updatedTask);
            saveToFile();
        });
    }
}
