package org.zeta.model;

import java.util.UUID;

public class Task {

    private String id;
    private String projectId;
    private String taskName;
    private String builderId;
    private TaskStatus status;


    public Task() {
    }
    public Task(String projectId, String taskName) {
        this.id= UUID.randomUUID().toString();
        this.projectId = projectId;
        this.taskName = taskName;
        this.status = TaskStatus.NOT_STARTED;
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getBuilderId() {
        return builderId;
    }

    public TaskStatus getStatus() {
        return status;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", taskName='" + taskName + '\'' +
                ", builderId=" + builderId +
                ", status=" + status +
                '}';
    }
}
