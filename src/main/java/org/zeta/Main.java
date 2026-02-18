package org.zeta;

import org.zeta.dao.UserDao;
import org.zeta.model.enums.Role;
import org.zeta.model.User;
import org.zeta.service.implementation.AuthenticationService;
import org.zeta.validation.CommonValidator;
import org.zeta.validation.ValidationException;
import org.zeta.views.BuilderView;
import org.zeta.views.ClientView;
import org.zeta.views.ProjectManagerView;

import java.io.Console;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Logger logger = Logger.getLogger("main");

        UserDao userDAO = new UserDao();
        AuthenticationService authService = new AuthenticationService(userDAO);

        while (true) {

            System.out.println("\nPlease choose:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit application");

            try {

                String input = scanner.nextLine();
                int choice = CommonValidator.validateInteger(input, "Menu choice");

                switch (choice) {

                    case 1 -> {
                        System.out.println("Enter username:");
                        String regUsername = scanner.nextLine();

                        Console console = System.console();
                        String regPassword;
                        String confirmPassword;

                        if (console != null) {
                            regPassword = new String(console.readPassword("Enter password: "));
                            confirmPassword = new String(console.readPassword("Confirm password: "));
                        } else {
                            // IntelliJ fallback
                            System.out.println("Enter password:");
                            regPassword = scanner.nextLine();

                            System.out.println("Confirm password:");
                            confirmPassword = scanner.nextLine();
                        }

                        Role selectedRole = null;
                        while (selectedRole == null) {
                            System.out.println("""
                                    Select Role:
                                    1. Builder
                                    2. Project Manager
                                    3. Client
                                    Enter your choice:
                                    """);

                            String roleInput = scanner.nextLine();
                            int roleChoice =
                                    CommonValidator.validateInteger(roleInput, "Role choice");

                            switch (roleChoice) {
                                case 1 -> selectedRole = Role.BUILDER;
                                case 2 -> selectedRole = Role.PROJECT_MANAGER;
                                case 3 -> selectedRole = Role.CLIENT;
                                default -> System.out.println("Please select a valid option (1-3).");
                            }
                        }

                        boolean registered = authService.register(
                                regUsername, regPassword, confirmPassword, selectedRole
                        );

                        if (registered) {
                            System.out.println("Registration successful!");
                        } else {
                            System.out.println("Registration failed.");
                        }
                    }

                    case 2 -> {
                        System.out.println("Enter username:");
                        String loginUsername = scanner.nextLine();

                        Console console = System.console();
                        String loginPassword;

                        if (console != null) {
                            loginPassword = new String(console.readPassword("Enter password: "));
                        } else {
                            System.out.println("Enter password:");
                            loginPassword = scanner.nextLine();
                        }

                        try {
                            User loggedInUser = authService.login(loginUsername, loginPassword);
                            System.out.println("Welcome " + loggedInUser.getUsername());

                            Role role = loggedInUser.getRole();

                            if (role == Role.CLIENT) {
                                ClientView.clientDashboard(loggedInUser, scanner);
                            } else if (role == Role.BUILDER) {
                                BuilderView.builderDashboard(loggedInUser, scanner);
                            } else if (role == Role.PROJECT_MANAGER) {
                                ProjectManagerView.ProjectManagerDashboard(loggedInUser, scanner);
                            }

                        } catch (ValidationException e) {
                            logger.severe("Login failed: " + e.getMessage());
                        }
                    }

                    case 3 -> {
                        System.out.println("Exiting application...");
                        scanner.close();
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }

            } catch (ValidationException ve) {
                System.out.println("Error: " + ve.getMessage());
            }
        }
    }
}
