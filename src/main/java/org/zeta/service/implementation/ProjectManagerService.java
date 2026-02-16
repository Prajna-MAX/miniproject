package org.zeta.service.implementation;

import org.zeta.dao.BaseDao;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.Role;
import org.zeta.model.Task;
import org.zeta.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class ProjectManagerService {
    private static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(ProjectManagerService.class.getName());


    public static void listProjects(BaseDao<Project> projectDao, User manager) {
        List<Project> projects = projectDao.getAll();
        System.out.println("\n--- Your Projects ---");
        for (Project p : projects) {
            if (p.getProjectManagerId().equals(manager.getId())) {
                System.out.println(p.getProjectId() + " - " + p.getProjectName());
            }
        }
    }
    public static void createTask(TaskDao taskDao, ProjectDao projectDao, User manager) {

        System.out.println("Enter Project ID to create task in:");
        String projectId = sc.nextLine().trim();

        Optional<Project> projectOpt = projectDao.findById(projectId);
        if (projectOpt.isEmpty()) {
            System.out.println("Project not found. Make sure to enter the correct ID!");
            return;
        }

        System.out.println("Enter Task Name:");
        String taskName = sc.nextLine().trim();

        Task newTask = new Task(projectId,taskName);
        taskDao.add(newTask);
        System.out.println("Task created successfully with ID: " + newTask.getId());
    }


    public static void assignTask(TaskDao taskDao, UserDao userDao) {
        System.out.println("Enter Project ID to assign tasks:");
        String projectId = sc.nextLine().trim();

        List<Task> tasks = taskDao.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            System.out.println("No tasks found for this project.");
            return;
        }

        System.out.println("\n--- Tasks ---");
        for (Task t : tasks) {
            if (t.getBuilderId() == null) {
                System.out.println(t.getId() + " - " + t.getTaskName());
            }
        }

        System.out.println("Enter Task ID to assign:");
        String taskId = sc.nextLine().trim();

        List<User> builders = userDao.findByRole(Role.BUILDER);
        if (builders.isEmpty()) {
            System.out.println("No builders available.");
            return;
        }

        System.out.println("\n--- Builders ---");
        for (User b : builders) {
            System.out.println(b.getId() + " - " + b.getUsername());
        }

        System.out.println("Enter Builder ID to assign:");
        String builderId = sc.nextLine().trim();

        if(builders.stream().anyMatch(b->b.getId().equals(builderId))) {
            taskDao.assignBuilder(taskId, builderId);
            System.out.println("Builder assigned to task successfully.");
        }
        else {
            logger.warning("Invalid Builder ID. Assignment failed.");
        }
    }

    public static void listClients(UserDao userDao) {
        List<User> clients = userDao.findByRole(Role.CLIENT);
        System.out.println("\n--- Clients ---");
        for (User c : clients) {
            System.out.println(c.getId() + " - " + c.getUsername());
        }
    }

    public static void viewProjectsByClient(UserDao userDao, BaseDao<Project> projectDao) {
        System.out.println("Enter Client Username:");
        String username = sc.nextLine().trim();

        Optional<User> clientOpt = userDao.findIdbyName(username);
        if (!clientOpt.isPresent()) {
            System.out.println("No client found with username: " + username);
            return;
        }

        String clientId = clientOpt.get().getId();
        List<Project> clientProjects = projectDao.getAll();

        System.out.println("\n--- Projects for Client: " + username + " ---");
        for (Project p : clientProjects) {
            if (clientId.equals(p.getClientId())) {
                System.out.println(p.getProjectId() + " - " + p.getProjectName());
            }
        }
    }
}
