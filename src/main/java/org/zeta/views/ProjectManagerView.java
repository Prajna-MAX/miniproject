package org.zeta.views;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.User;
import org.zeta.service.implementation.ProjectManagerService;
import org.zeta.validation.CommonValidator;
import org.zeta.validation.ValidationException;

import java.util.Scanner;
import java.util.logging.Logger;

public class ProjectManagerView {

    static Scanner sc = new Scanner(System.in);
    private static final Logger logger =
            Logger.getLogger(ProjectManagerView.class.getName());

    static ProjectManagerService managerService =
            new ProjectManagerService();

    public static void ProjectManagerDashboard(User projectManager) {

        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();
        TaskDao taskDao = new TaskDao();

        System.out.println("Hi Project Manager: " + projectManager.getUsername());

        boolean running = true;

        while (running) {

            System.out.println("""
                    
                    ===== Project Manager Dashboard =====
                    1. View your upcoming projects
                    2. Add project details (Start project)
                    3. Create tasks for a project
                    4. Assign tasks to builders
                    5. List clients
                    6. View projects by client
                    7. Logout
                    Enter your choice:
                    """);

            try {

                String input = sc.nextLine();
                int choice = CommonValidator.validateInteger(input, "Menu choice");

                switch (choice) {

                    case 1:
                        managerService.listProjects(projectDao, projectManager);
                        break;

                    case 2:

                        System.out.println("Enter Project ID to update:");
                        String projectId = sc.nextLine().trim();
                        System.out.println("Enter Project Description:");
                        String description = sc.nextLine().trim();
                        managerService.addProjectDetails(projectId,description,projectDao, projectManager);
                        break;

                    case 3:System.out.println("Enter Project ID to create task in:");
                        String projectIdForTask = sc.nextLine().trim();

                        managerService.createTask(projectIdForTask,taskDao, projectDao, projectManager);
                        break;

                    case 4:
                        System.out.println("Enter Project ID to assign tasks:");
                        String projectid = sc.nextLine().trim();

                        managerService.assignTask(projectid,taskDao, userDao);
                        break;

                    case 5:
                        managerService.listClients(userDao);
                        break;

                    case 6:
                        System.out.println("Enter Client Username:");
                        String username = sc.nextLine().trim();
                        managerService.viewProjectsByClient(username,userDao, projectDao);
                        break;

                    case 7:
                        System.out.println("Logging out...");
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
