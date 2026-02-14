package org.zeta.model;
import java.time.LocalDate;
import java.util.UUID;

public class Project {

    private String projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private String clientId;
    private String projectManagerId;
    private String builderId;

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
                   LocalDate endDate,
                   ProjectStatus status,
                   String clientId,
                   String projectManagerId,
                   String builderId) {

        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.clientId = clientId;
        this.projectManagerId = projectManagerId;
        this.builderId = builderId;
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }
}
