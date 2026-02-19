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

    private static final Scanner sc = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(ProjectManagerView.class.getName());
    private static final ProjectManagerService managerService = new ProjectManagerService();

    public static void ProjectManagerDashboard(User projectManager,Scanner sc) {

        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();
        TaskDao taskDao = new TaskDao();

        System.out.println("\n===== Welcome, Project Manager: " + projectManager.getUsername() + " =====\n");

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
                    """);
            System.out.print("Enter your choice: ");
            System.out.flush(); // flush stdout before logging

            try {
                String input = sc.nextLine().trim();
                int choice = CommonValidator.validateInteger(input, "Menu choice");
                logger.info("Project Manager selected menu option: " + choice);

                switch (choice) {

                    case 1 -> managerService.listProjects(projectDao, projectManager);

                    case 2 -> {
                        System.out.println("\n--- Upcoming Projects ---");

                        managerService.listProjects(projectDao, projectManager);

                        System.out.print("\nEnter Project ID to update: ");
                        String projectId = sc.nextLine().trim();

                        System.out.print("Enter Project Description: ");
                        String description = sc.nextLine().trim();

                        System.out.print("Enter project duration (in days): ");
                        int durationInput = sc.nextInt();
                        sc.nextLine();

                        boolean updated = managerService.addProjectDetails(
                                projectId,
                                description,
                                durationInput,
                                projectDao,
                                projectManager
                        );

                        if (updated) {
                            System.out.println("Project updated successfully and moved to IN_PROGRESS.");
                        } else {
                            System.out.println("Project update failed. Check logs.");
                        }
                    }


                    case 3 -> {
                        System.out.println("\n--- Create Task ---");
                        System.out.print("Enter Project ID to create task in: ");
                        System.out.flush();
                        String projectIdForTask = sc.nextLine().trim();

                        System.out.print("Enter Task Name: ");
                        System.out.flush();
                        String taskName = sc.nextLine().trim();

                        managerService.createTask(projectIdForTask, taskName, taskDao, projectDao, projectManager);
                    }

                    case 4 -> {
                        System.out.println("\n--- Assign Task to Builder ---");
                        System.out.print("Enter Project ID: ");
                        System.out.flush();
                        String projectId = sc.nextLine().trim();

                        System.out.print("Enter Task ID: ");
                        System.out.flush();
                        String taskId = sc.nextLine().trim();

                        System.out.print("Enter Builder ID: ");
                        System.out.flush();
                        String builderId = sc.nextLine().trim();

                        managerService.assignTask(projectId, taskId, builderId, taskDao, userDao);
                        System.out.println("Task assignment attempted.");
                        System.out.flush();
                        logger.info("Task assignment attempted: taskId=" + taskId + ", builderId=" + builderId);
                    }

                    case 5 -> {
                        System.out.println("\n--- List of Clients ---");
                        System.out.flush();
                        managerService.listClients(userDao);
                        logger.info("Displayed client list to Project Manager.");
                    }

                    case 6 -> {
                        System.out.println("\n--- View Projects by Client ---");
                        System.out.print("Enter Client Username: ");
                        System.out.flush();
                        String username = sc.nextLine().trim();

                        managerService.viewProjectsByClient(username, userDao, projectDao);
                        logger.info("Viewed projects for client: " + username);
                    }

                    case 7 -> {
                        System.out.println("\nLogging out... Goodbye!");
                        System.out.flush();
                        logger.info("Project Manager " + projectManager.getUsername() + " logged out.");
                        running = false;
                    }

                    default -> {
                        System.out.println("Invalid choice. Please select a valid option (1-7).");
                        System.out.flush();
                        logger.warning("Invalid menu choice entered: " + choice);
                    }
                }

            } catch (ValidationException e) {
                System.out.println("\nError: " + e.getMessage());
                System.out.flush();
                logger.warning("Validation error in ProjectManagerDashboard: " + e.getMessage());
            }
        }
    }
}