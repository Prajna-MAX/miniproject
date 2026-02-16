package org.zeta.service.interfaces;

import org.zeta.model.Project;

import java.util.List;

public interface IClientService {

    void submitProject(String projectName, String clientId);

    List<Project> getClientProjects(String clientId);
}
