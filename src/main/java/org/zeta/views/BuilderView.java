package org.zeta.views;

import org.zeta.model.User;
import org.zeta.service.implementation.BuilderService;
import org.zeta.validation.CommonValidator;
import org.zeta.validation.ValidationException;

import java.util.Scanner;

public class BuilderView {
    static BuilderService builderService = new BuilderService();
    public static void builderDashboard(User builder, Scanner sc) {

        boolean running = true;
        System.out.println("HI Builder");

        while (running) {
            System.out.println("""
                    
                    ===== Builder Dashboard =====
                    1. View my tasks
                    2. Update tasks
                    3. Logout
                    Enter your choice:
                    """);
            try {

                String input = sc.nextLine();
                int builderChoice = CommonValidator.validateInteger(input, "Builder menu choice");

                switch (builderChoice) {

                    case 1 -> {
                        System.out.println("\n--- Your Tasks ---");
                        BuilderService.listOfTasks(builder);
                    }
                    case 2 -> {
                        System.out.println("Enter Task Name:");
                        String taskName = sc.nextLine();
                        BuilderService.updateStatus(taskName, builder);
                    }
                    case 3 -> {
                        System.out.println("Logging out...");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (ValidationException ve) {
                System.out.println("Error: " + ve.getMessage());
            }
        }
    }
}
