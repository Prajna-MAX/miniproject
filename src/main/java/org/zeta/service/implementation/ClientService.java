package org.zeta.service.implementation;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.Role;
import org.zeta.model.User;
import org.zeta.service.interfaces.IClientService;
import org.zeta.validation.ProjectValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        ProjectValidator.validateProjectCreation(projectName, clientId);

        Project project = new Project(projectName, clientId);

        List<User> managers = userDao.findByRole(Role.PROJECT_MANAGER);

        if (managers.isEmpty()) {
            throw new RuntimeException("No Project Managers available.");
        }

        User randomManager =
                managers.get(new Random().nextInt(managers.size()));

        project.setProjectManagerId(randomManager.getId());

        projectDao.saveProject(project);

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedTime = now.format(formatter);

        System.out.println("Project created successfully at " + formattedTime);
    }


    @Override
    public List<Project> getClientProjects(String clientId) {
        return projectDao.findByClient(clientId);
    }
}
