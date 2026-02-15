package org.zeta.model;
import java.time.LocalDate;
import java.util.UUID;

public class Project {

    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private PROJECT_STATUS status;
    private String clientId;
    private String builderId;


    public Project(String id,
                   String name,
                   String description,
                   LocalDate startDate,
                   PROJECT_STATUS status,
                   String clientId,
                   String builderId) {

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.startDate = LocalDate.now();
        this.status = status;
        this.clientId = clientId;
        this.builderId = builderId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public PROJECT_STATUS getStatus() {
        return status;
    }

    public void setStatus(PROJECT_STATUS status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBuilderId() {
        return builderId;
    }

    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }
}

