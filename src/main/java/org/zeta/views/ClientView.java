package org.zeta.views;

import org.zeta.dao.BaseDao;
import org.zeta.dao.ProjectDao;
import org.zeta.model.Project;
import org.zeta.model.User;
import org.zeta.service.ClientService;

import java.util.List;
import java.util.Scanner;

public class ClientView {
    public static void clientDashboard(User client) {
        Scanner sc = new Scanner(System.in);
        System.out.println("HEY Client!");
        boolean running = true;
        ClientService clientService = new ClientService();
        while (running) {

            System.out.println("Enter 1 if you want to submit a project\nEnter 2 if you want to view your project's update\n3 Logout");
            int clientChoice = sc.nextInt();
            sc.nextLine();

            switch (clientChoice) {
                case 1:
                    System.out.println("Enter Project Name");
                    String projectName = sc.nextLine();
                    clientService.projectSubmit(projectName, client.getId());
                    break;


                case 2:
                    clientService.ClientProject(client.getId());


                    break;

                case 3:
                    System.out.println("Logging out...");
                    running = false;
                    break;
            }
        }
    }
}
