package org.zeta.model;
import java.time.LocalDate;
import java.util.UUID;

public class Project {

    private String projectId;
    private String projectName;
    private LocalDate startDate;
    private ProjectStatus status;
    private String clientId;
    private String projectManagerId;

    public Project() {
    }

    public Project(String projectName,
                   String clientId) {
        this.projectId = UUID.randomUUID().toString();
        this.projectName = projectName;
        this.clientId = clientId;
        this.status=ProjectStatus.Submitted;
    }


    public Project(String projectId,
                   String projectName,
                   LocalDate startDate,
                   ProjectStatus status,
                   String clientId,
                   String projectManagerId) {

        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
        this.status = status;
        this.clientId = clientId;
        this.projectManagerId = projectManagerId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
    }


}
