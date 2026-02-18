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
        System.out.println("\n===== Welcome, " + client.getUsername() + " =====\n");

        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();
        ClientService clientService = new ClientService(projectDao, userDao);

        boolean running = true;

        while (running) {
            System.out.println("Please choose an option:");
            System.out.println("  1) Submit a new project");
            System.out.println("  2) View your project updates");
            System.out.println("  3) Logout");
            System.out.print("Enter choice (1-3): ");

            try {
                String input = sc.nextLine();
                int clientChoice = CommonValidator.validateInteger(input, "Menu choice");

                switch (clientChoice) {

                    case 1:
                        System.out.print("Enter Project Name: ");
                        String projectName = sc.nextLine().trim();
                        clientService.submitProject(projectName, client.getId());
                        System.out.println("Project submitted successfully!\n");
                        break;

                    case 2:
                        List<Project> projects = clientService.getClientProjects(client.getId());

                        if (projects.isEmpty()) {
                            System.out.println("You currently have no projects.\n");
                        } else {
                            System.out.println("----- Your Projects -----");
                            for (Project p : projects) {
                                System.out.printf("• [%s] %s%n", p.getProjectId(), p.getProjectName());
                            }
                            System.out.println();
                        }
                        break;

                    case 3:
                        System.out.println("Logging out. Goodbye!");
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please select 1, 2, or 3.\n");
                }

            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }

        sc.close();
    }
}
