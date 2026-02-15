package org.zeta.service;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.Role;
import org.zeta.model.User;

import java.util.List;
import java.util.Random;

public class ClientService {
    ProjectDao projectDao = new ProjectDao();
    UserDao userDao=new UserDao();
    public void projectSubmit(String projectName, String clientId) {
        Project project = new Project(projectName, clientId);
        List<User> managers = userDao.findByRole(Role.PROJECT_MANAGER);

        if (managers.isEmpty()) {
            System.out.println("No Project Managers available. Cannot submit project.");
            return;
        }

        Random random = new Random();
        User randomManager = managers.get(random.nextInt(managers.size()));

        project.setProjectManagerId(randomManager.getId());

        projectDao.saveProject(project);

        System.out.println("Project with " + project.getProjectName() +
                " submitted and assigned to Manager: " +
                randomManager.getUsername());
    }


    public void ClientProject(String clientId) {
        List<Project> clientProject = projectDao.findByClient(clientId);
        clientProject.forEach(p ->
                System.out.println(p.getProjectName())
        );
    }
}