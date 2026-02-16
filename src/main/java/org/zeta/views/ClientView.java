package org.zeta.views;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.UserDao;
import org.zeta.model.User;
import org.zeta.service.implementation.ClientService;

import java.util.Scanner;

public class ClientView {
    public static void clientDashboard(User client) {
        Scanner sc = new Scanner(System.in);
        System.out.println("HEY Client!");
        boolean running = true;
        ProjectDao projectDao = new ProjectDao();
        UserDao userDao = new UserDao();

        ClientService clientService = new ClientService(projectDao, userDao);

        while (running) {

            System.out.println("Enter 1 if you want to submit a project\nEnter 2 if you want to view your project's update\n3 Logout");
            int clientChoice = sc.nextInt();
            sc.nextLine();

            switch (clientChoice) {
                case 1:
                    System.out.println("Enter Project Name");
                    String projectName = sc.nextLine();
                    clientService.submitProject(projectName, client.getId());
                    break;


                case 2:
                    clientService.getClientProjects(client.getId());


                    break;

                case 3:
                    System.out.println("Logging out...");
                    running = false;
                    break;
            }
        }
    }
}
