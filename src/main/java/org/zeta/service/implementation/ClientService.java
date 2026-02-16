package org.zeta.service.implementation;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.Role;
import org.zeta.model.User;
import org.zeta.service.interfaces.IClientService;

import java.util.List;
import java.util.Random;

public class ClientService implements IClientService {

    private final ProjectDao projectDao;
    private final UserDao userDao;

    public ClientService(ProjectDao projectDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.userDao = userDao;
    }

    @Override
    public void submitProject(String projectName, String clientId) {

        Project project = new Project(projectName, clientId);

        List<User> managers =
                userDao.findByRole(Role.PROJECT_MANAGER);

        if (managers.isEmpty()) {
            throw new RuntimeException("No Project Managers available.");
        }

        User randomManager =
                managers.get(new Random().nextInt(managers.size()));

        project.setProjectManagerId(randomManager.getId());

        projectDao.saveProject(project);
    }

    @Override
    public List<Project> getClientProjects(String clientId) {
        return projectDao.findByClient(clientId);
    }
}
