package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import org.zeta.model.Project;

import java.util.List;
import java.util.Optional;

public class ProjectDao extends BaseDao<Project> {

    public ProjectDao() {
        super("projects.json", new TypeReference<List<Project>>() {});
    }

    public ProjectDao(String fileName) {
        super(fileName, new TypeReference<List<Project>>() {});
    }

    public void saveProject(Project project) {
        add(project);
    }

    public void deleteProject(Project project) {
        remove(project);
    }

    public Optional<Project> findById(String projectId) {
        return dataList.stream()
                .filter(p -> p.getProjectId().equals(projectId))
                .findFirst();
    }

    public List<Project> findByProjectManager(String managerId) {
        return dataList.stream()
                .filter(p -> managerId.equals(p.getProjectManagerId()))
                .toList();
    }

    public List<Project> findByClient(String clientId) {
        return dataList.stream()
                .filter(p -> clientId.equals(p.getClientId()))
                .toList();
    }

    public List<Project> findByStatus(String status) {
        return dataList.stream()
                .filter(p -> p.getStatus().name().equalsIgnoreCase(status))
                .toList();
    }

    public void update(Project updatedProject) {
        findById(updatedProject.getProjectId()).ifPresent(existing -> {
            dataList.remove(existing);
            dataList.add(updatedProject);
            saveToFile();
        });
    }
}
