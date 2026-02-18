package org.zeta.views;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.enums.ProjectStatus;
import org.zeta.model.enums.Role;
import org.zeta.model.enums.TaskStatus;
import org.zeta.model.User;
import org.zeta.service.implementation.ProjectManagerService;
import org.zeta.validation.CommonValidator;
import org.zeta.validation.ValidationException;
import java.util.List;
import org.zeta.model.Project;
import org.zeta.model.Task;
import java.util.Scanner;
import java.util.logging.Logger;

public class ProjectManagerView {

    private static final Logger logger =
            Logger.getLogger(ProjectManagerView.class.getName());
    static ProjectManagerService managerService =
            new ProjectManagerService();

    public static void ProjectManagerDashboard(User projectManager, Scanner sc) {

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
                    5. View projects by client
                    6. Logout
                    Enter your choice:
                    """);
            try {
                String input = sc.nextLine();
                int choice = CommonValidator.validateInteger(input, "Menu choice");

                switch (choice) {
                    case 1 -> {
                        managerService.listProjects(projectDao, projectManager);
                    }
                    case 2 -> {
                        System.out.println("Enter Project ID to update:");
                        String projectId = sc.nextLine().trim();

                        System.out.println("Enter Project Description:");
                        String description = sc.nextLine().trim();
                        System.out.println("Enter the duration for this project:");
                        String durationStr = sc.nextLine();
                        int duration = CommonValidator.validateInteger(durationStr, "Duration");

                        managerService.addProjectDetails(
                                projectId, description, duration, projectDao, projectManager
                        );
                    }
                    case 3 -> {
                        System.out.println("Enter Project ID to create task in:");
                        String projectIdForTask = sc.nextLine().trim();

                        System.out.println("Enter Task Name:");
                        String taskName = sc.nextLine().trim();

                        managerService.createTask(
                                projectIdForTask, taskName, taskDao, projectDao, projectManager
                        );
                    }
                    case 4 -> {

                        System.out.println("\n--- Your In-Progress Projects ---");
                        List<Project> inProgressProjects = projectDao.getAll().stream()
                                .filter(p -> p.getProjectManagerId().equals(projectManager.getId())
                                        && p.getStatus() == ProjectStatus.InProgress)
                                .toList();

                        if (inProgressProjects.isEmpty()) {
                            System.out.println("Nothing to display here. No in-progress projects.");
                            break;
                        }

                        inProgressProjects.forEach(p ->
                                System.out.println(p.getProjectId() + " - " + p.getProjectName())
                        );

                        System.out.println("Enter Project ID to assign tasks:");
                        String projectId = sc.nextLine().trim();
                        System.out.println("\n--- Not Started Tasks ---");

                        List<Task> notStartedTasks = taskDao.findByProjectId(projectId).stream()
                                .filter(t -> t.getStatus() == TaskStatus.NOT_STARTED)
                                .toList();

                        if (notStartedTasks.isEmpty()) {
                            System.out.println("Nothing to display here. No NOT_STARTED tasks.");
                            break;
                        }

                        notStartedTasks.forEach(t ->
                                System.out.println(t.getId() + " - " + t.getTaskName())
                        );

                        System.out.println("Enter Task ID to assign:");
                        String taskId = sc.nextLine().trim();

                        System.out.println("\n--- Available Builders ---");

                        List<User> builders = userDao.findByRole(Role.BUILDER);

                        if (builders.isEmpty()) {
                            System.out.println("Nothing to display here. No builders available.");
                            break;
                        }

                        builders.forEach(b ->
                                System.out.println(b.getId() + " - " + b.getUsername())
                        );

                        System.out.println("Enter Builder ID to assign:");
                        String builderId = sc.nextLine().trim();

                        managerService.assignTask(projectId, taskId, builderId, taskDao, userDao);
                    }


                    case 5 -> {
                        System.out.println("Enter Client Username:");
                        String username = sc.nextLine().trim();

                        managerService.viewProjectsByClient(username, userDao, projectDao);
                    }

                    case 6 -> {
                        System.out.println("Logging out...");
                        running = false;
                    }

                    default -> System.out.println("Invalid choice. Please try again.");
                }

            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
