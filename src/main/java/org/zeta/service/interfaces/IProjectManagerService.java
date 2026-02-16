package org.zeta.service.interfaces;

import org.zeta.model.Project;
import org.zeta.model.Task;

import java.util.List;

public interface IProjectManagerService {

    List<Project> getProjectsByManager(String managerId);

    Task createTask(String projectId, String taskName);

    void assignTask(String taskId, String builderId);

    List<Task> getTasksByProject(String projectId);
}
