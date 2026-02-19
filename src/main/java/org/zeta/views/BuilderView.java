package org.zeta.views;

import org.zeta.model.User;
import org.zeta.service.implementation.BuilderService;
import org.zeta.validation.ValidationException;

import java.util.Scanner;
import java.util.logging.Logger;

public class BuilderView {

    private static final BuilderService builderService = new BuilderService();
    private static final Logger logger = Logger.getLogger(BuilderView.class.getName());

    public static void builderDashboard(User builder, Scanner sc) {

        boolean running = true;

        System.out.println("\n===== Welcome, Builder: " + builder.getUsername() + " =====\n");

        while (running) {

            System.out.println("""
                    
                    ===== Builder Dashboard =====
                    1. View my tasks
                    2. Update task status
                    3. Logout
                    """);
            System.out.print("Enter your choice: ");
            System.out.flush();

            try {
                int builderChoice = sc.nextInt();
                sc.nextLine(); // consume newline

                logger.info("Builder selected menu option: " + builderChoice);

                switch (builderChoice) {

                    case 1 -> {
                        System.out.println("\n--- Your Tasks ---\n");
                        System.out.flush();
                        BuilderService.listOfTasks(builder);
                        logger.info("Displayed tasks for builder: " + builder.getUsername());
                    }

                    case 2 -> {
                        System.out.println("\n--- Update Task Status ---");
                        System.out.print("Enter Task Name to update: ");
                        System.out.flush();
                        String taskName = sc.nextLine().trim();

                        BuilderService.updateStatus(taskName, builder);
                        System.out.println("Task status updated successfully.");
                        System.out.flush();
                        logger.info("Builder " + builder.getUsername() + " updated task: " + taskName);
                    }

                    case 3 -> {
                        System.out.println("\nLogging out... Goodbye!");
                        System.out.flush();
                        logger.info("Builder " + builder.getUsername() + " logged out.");
                        running = false;
                    }

                    default -> {
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        System.out.flush();
                        logger.warning("Invalid menu option entered by builder: " + builderChoice);
                    }
                }

            } catch (ValidationException ve) {
                System.out.println("\nError: " + ve.getMessage());
                System.out.flush();
                logger.warning("Validation error in BuilderDashboard: " + ve.getMessage());
            } catch (Exception e) {
                System.out.println("\nUnexpected error: " + e.getMessage());
                System.out.flush();
                logger.severe("Unexpected error in BuilderDashboard: " + e.getMessage());
                sc.nextLine(); // clear scanner buffer
            }
        }
    }
}
