package org.zeta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeta.dao.UserDao;
import org.zeta.model.Role;
import org.zeta.model.User;
import org.zeta.service.implementation.AuthenticationService;
import org.zeta.validation.CommonValidator;
import org.zeta.validation.ValidationException;
import org.zeta.views.BuilderView;
import org.zeta.views.ClientView;
import org.zeta.views.ProjectManagerView;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        Logger logger = Logger.getLogger("main");
        UserDao userDAO = new UserDao();
        AuthenticationService authService = new AuthenticationService(userDAO);
        ObjectMapper mapper = new ObjectMapper();

        while (true) {

            System.out.println("Please choose:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit application");
            try {
                String input = sc.nextLine();
                int choice = CommonValidator.validateInteger(input, "Menu choice");

                switch (choice) {

                    case 1:
                        try {
                            System.out.println("Enter username:");
                            String regUsername = sc.nextLine();

                            System.out.println("Enter password:");
                            String regPassword = sc.nextLine();

                            System.out.println("Confirm password:");
                            String confirmPassword = sc.nextLine();

                            System.out.println("Enter role (BUILDER / PROJECT_MANAGER / CLIENT):");
                            String role = sc.nextLine().toUpperCase();

                            authService.register(
                                    regUsername,
                                    regPassword,
                                    confirmPassword,
                                    Role.valueOf(role)
                            );

                            System.out.println("Registration successful!");

                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid role entered.");
                        } catch (ValidationException e) {
                            logger.severe("Error: " + e.getMessage());
                        }
                        break;

                    case 2:
                        try {
                            System.out.println("Enter username:");
                            String loginUsername = sc.nextLine();

                            System.out.println("Enter password:");
                            String loginPassword = sc.nextLine();

                            User loggedInUser = authService.login(loginUsername, loginPassword);

                            if (loggedInUser != null) {
                                System.out.println("Welcome " + loggedInUser.getUsername());

                                switch (loggedInUser.getRole()) {
                                    case CLIENT:
                                        ClientView.clientDashboard(loggedInUser);
                                        break;
                                    case BUILDER:
                                        BuilderView.builderDashboard(loggedInUser);
                                        break;
                                    case PROJECT_MANAGER:
                                        ProjectManagerView.ProjectManagerDashboard(loggedInUser);
                                        break;
                                }
                            }

                        } catch (ValidationException e) {
                            logger.severe("Error: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("Exiting application...");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice");
                }}
catch (ValidationException ValidationException) {
                System.out.println("Error: " + ValidationException.getMessage());
            }
        }
    }
}
