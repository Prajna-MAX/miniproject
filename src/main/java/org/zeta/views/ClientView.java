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
        boolean running = true;

        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();
        IClientService clientService = new ClientService(projectDao, userDao);

        while (running) {
            System.out.println("""
                    1. Submit a project
                    2. View your project updates
                    3. Logout
                    Enter your choice:
                    """);

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

                    case 1:
                        System.out.println("Enter Project Name:");
                        String projectName = sc.nextLine();
                        clientService.submitProject(projectName, client.getId());
                        break;

                    case 2:
                        List<Project> projects =
                                clientService.getClientProjects(client.getId());

                        if (projects.isEmpty()) {
                            System.out.println("No projects found.");
                        } else {
                            System.out.println("<------- Here are your Projects -------->");
                            for (Project p : projects) {
                                System.out.println(p.getProjectId() + " - " + p.getProjectName());
                            }
                        }
                        break;

                    case 3:
                        System.out.println("Logging out...");
                        running = false;
                        break;

                    default:
                        System.out.println("Please select a valid option (1-3).");
                }

            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
