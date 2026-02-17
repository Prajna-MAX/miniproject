package org.zeta.views;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.User;
import org.zeta.service.implementation.ClientService;
import org.zeta.service.interfaces.IClientService;
import org.zeta.validation.CommonValidator;
import org.zeta.validation.ValidationException;

import java.util.List;
import java.util.Scanner;

public class ClientView {

    public static void clientDashboard(User client) {

        Scanner sc = new Scanner(System.in);
        System.out.println("HEY Client!");

        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();
        IClientService clientService = new ClientService(projectDao, userDao);

        boolean running = true;

        while (running) {
            System.out.println("""
                    1. Submit a project
                    2. View your project updates
                    3. Logout
                    Enter your choice:
                    """);

            try {
                String input = sc.nextLine();
                int clientChoice = CommonValidator.validateInteger(input, "Menu choice");

                switch (clientChoice) {
                    case 1 -> {
                        System.out.println("Submitting a project...");
                        // You can implement project submission here
                        System.out.println("Feature coming soon!");
                    }

                    case 2 -> {
                        List<Project> projects = clientService.getClientProjects(client.getId());

                        if (projects.isEmpty()) {
                            System.out.println("No projects found.");
                        } else {
                            System.out.println("<-------Here are your Projects-------->");
                            for (Project p : projects) {
                                System.out.println(p.getProjectId() + " - " + p.getProjectName());
                            }
                        }
                    }

                    case 3 -> {
                        System.out.println("Logging out...");
                        running = false;
                    }

                    default -> System.out.println("Invalid choice! Please select 1-3.");
                }

            } catch (ValidationException ve) {
                System.out.println("Error: " + ve.getMessage());
            } catch (NumberFormatException nfe) {
                System.out.println("Error: Enter a valid integer.");
            }
        }

        sc.close();
    }
}
