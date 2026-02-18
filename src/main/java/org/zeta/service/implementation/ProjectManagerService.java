package org.zeta.service.implementation;

import org.zeta.dao.BaseDao;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.ProjectStatus;
import org.zeta.model.Role;
import org.zeta.model.Task;
import org.zeta.model.User;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ProjectManagerService {

    private static final Logger logger =
            Logger.getLogger(ProjectManagerService.class.getName());



    private static boolean isAuthorized(Project project, User manager) {
        return project.getProjectManagerId().equals(manager.getId());
    }


    public static void listProjects(BaseDao<Project> projectDao, User manager) {

        List<Project> projects = projectDao.getAll();
        boolean found = false;

        System.out.println("\n--- Upcoming Projects ---");

        for (Project p : projects) {
            if (p.getProjectManagerId().equals(manager.getId())
                    && p.getStatus() == ProjectStatus.Upcoming) {

                System.out.println(p.getProjectId() + " - " + p.getProjectName());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No upcoming projects found.");
        }
    }

    public static void createTask(String projectId,
                                  String taskName,
                                  TaskDao taskDao,
                                  ProjectDao projectDao,
                                  User manager) {


        Optional<Project> projectOpt = projectDao.findById(projectId);

        if (projectOpt.isEmpty()) {
            System.out.println("Project not found.");
            return;
        }

        Project project = projectOpt.get();

        if (!isAuthorized(project, manager)) {
            System.out.println("You are not authorized to create tasks for this project.");
            return;
        }

        if (project.getStatus() != ProjectStatus.InProgress) {
            System.out.println("This project is not available to add tasks.");
            return;
        }

        Task newTask = new Task(projectId, taskName);
        taskDao.add(newTask);

        System.out.println("Task created successfully with ID: " + newTask.getId());
    }

    public static void assignTask(String projectId,
                                  String taskId,
                                  String builderId,
                                  TaskDao taskDao,
                                  UserDao userDao) {

        List<Task> tasks = taskDao.findByProjectId(projectId);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found for this project.");
            return;
        }

        System.out.println("\n--- Available Tasks ---");
        for (Task t : tasks) {
            if (t.getBuilderId() == null) {
                System.out.println(t.getId() + " - " + t.getTaskName());
            }
        }

        List<User> builders = userDao.findByRole(Role.BUILDER);

        if (builders.isEmpty()) {
            System.out.println("No builders available.");
            return;
        }

        System.out.println("\n--- Builders ---");
        for (User b : builders) {
            System.out.println(b.getId() + " - " + b.getUsername());
        }

        boolean builderExists = builders.stream()
                .anyMatch(b -> b.getId().equals(builderId));

        if (builderExists) {
            taskDao.assignBuilder(taskId, builderId);
            System.out.println("Builder assigned to task successfully.");
        } else {
            logger.warning("Invalid Builder ID. Assignment failed.");
        }
    }


    public static void listClients(UserDao userDao) {

        List<User> clients = userDao.findByRole(Role.CLIENT);

        System.out.println("\n--- Clients ---");

        if (clients.isEmpty()) {
            System.out.println("No clients available.");
            return;
        }

        for (User c : clients) {
            System.out.println(c.getId() + " - " + c.getUsername());
        }
    }

    public static void viewProjectsByClient(String username,
                                            UserDao userDao,
                                            BaseDao<Project> projectDao) {

        Optional<User> clientOpt = userDao.findIdbyName(username);

        if (clientOpt.isEmpty()) {
            System.out.println("No client found with username: " + username);
            return;
        }

        String clientId = clientOpt.get().getId();
        List<Project> allProjects = projectDao.getAll();

        System.out.println("\n--- Projects for Client: " + username + " ---");

        boolean found = false;

        for (Project p : allProjects) {
            if (clientId.equals(p.getClientId())) {
                System.out.println(p.getProjectId() + " - " + p.getProjectName());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No projects found for this client.");
        }
    }

    public static void addProjectDetails(String projectId,
                                         String description,
                                         int durationInput,
                                         ProjectDao projectDao,
                                         User manager) {

        Optional<Project> projectOpt = projectDao.findById(projectId);

        if (projectOpt.isEmpty()) {
            System.out.println("Project not found.");
            return;
        }

        Project project = projectOpt.get();
        if (!isAuthorized(project, manager)) {
            logger.warning("You are not authorized to modify this project.");
            return;
        }
        if (project.getStatus() == ProjectStatus.InProgress) {
            System.out.println("Project is already in progress.");
            return;
        }
        project.setDescription(description);
        project.setDuration(durationInput);
        project.setStatus(ProjectStatus.InProgress);

        projectDao.update(project);

        System.out.println("Project updated successfully. Status set to IN_PROGRESS.");
    }
}
