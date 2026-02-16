package org.zeta.views;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.User;
import org.zeta.service.implementation.ProjectManagerService;
import java.util.Scanner;
import java.util.logging.Logger;

public class ProjectManagerView {
    static Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(ProjectManagerView.class.getName());
    static ProjectManagerService managerService = new ProjectManagerService();

    public static void ProjectManagerDashboard(User projectManager) {
        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();
        TaskDao taskDao = new TaskDao();

        System.out.println("Hi Project Manager: " + projectManager.getUsername());

        boolean running = true;
        while (running) {
            System.out.println("""
                    \n===== Project Manager Dashboard =====
                    1. View your projects
                    2. Create tasks for a project
                    3. Assign tasks to builders
                    4. List clients
                    5. View projects by client
                    6. Logout
                    Enter your choice:""");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    managerService.listProjects(projectDao, projectManager);
                    break;
                case 2:
                    managerService.createTask(taskDao, projectDao, projectManager);
                    break;
                case 3:
                    managerService.assignTask(taskDao, userDao);
                    break;
                case 4:
                    managerService.listClients(userDao);
                    break;
                case 5:
                    managerService.viewProjectsByClient(userDao, projectDao);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
