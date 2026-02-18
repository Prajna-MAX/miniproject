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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        Logger logger = Logger.getLogger(Main.class.getName());
        UserDao userDAO = new UserDao();
        AuthenticationService authService = new AuthenticationService(userDAO);

        boolean running = true;

        while (running) {
            System.out.println("\n===== Welcome to Project Management System =====\n");
            System.out.println("Please choose an option:");
            System.out.println("  1. Register");
            System.out.println("  2. Login");
            System.out.println("  3. Exit application");
            System.out.print("Enter your choice: ");

            try {
                String input = sc.nextLine().trim();
                int choice = CommonValidator.validateInteger(input, "Menu choice");
                logger.info("User selected menu choice: " + choice);

                switch (choice) {

                    case 1 -> {
                        System.out.println("\n--- User Registration ---");
                        System.out.print("Enter username: ");
                        String regUsername = sc.nextLine().trim();

                        System.out.print("Enter password: ");
                        String regPassword = sc.nextLine();

                        System.out.print("Confirm password: ");
                        String confirmPassword = sc.nextLine();

                        Role selectedRole = null;
                        while (selectedRole == null) {
                            System.out.println("""
                                    
                                    Select Role:
                                    1. Builder
                                    2. Project Manager
                                    3. Client
                                    """);
                            System.out.print("Enter your choice: ");

                            if (!sc.hasNextInt()) {
                                System.out.println("Invalid input. Please enter a number.");
                                logger.warning("User entered non-integer value for role selection.");
                                sc.nextLine(); // consume invalid input
                                continue;
                            }
                            int roleChoice = sc.nextInt();
                            sc.nextLine();

                            switch (roleChoice) {
                                case 1 -> selectedRole = Role.BUILDER;
                                case 2 -> selectedRole = Role.PROJECT_MANAGER;
                                case 3 -> selectedRole = Role.CLIENT;
                                default -> {
                                    System.out.println("Please select a valid option (1-3).");
                                    logger.warning("User selected invalid role option: " + roleChoice);
                                }
                            }
                        }

                        boolean registered = authService.register(regUsername, regPassword, confirmPassword, selectedRole);
                        if (registered) {
                            logger.info("New user registered with username: " + regUsername + ", role: " + selectedRole);
                        } else {
                            logger.warning("Registration failed for username: " + regUsername);
                        }
                        System.out.println();
                    }

                    case 2 -> {
                        System.out.println("\n--- User Login ---");
                        System.out.print("Enter username: ");
                        String loginUsername = sc.nextLine().trim();

                        System.out.print("Enter password: ");
                        String loginPassword = sc.nextLine();

                        try {
                            User loggedInUser = authService.login(loginUsername, loginPassword);
                            System.out.println("\nWelcome, " + loggedInUser.getUsername() + "!\n");
                            logger.info("User logged in: " + loginUsername);

                            Role role = loggedInUser.getRole();
                            if (role == Role.CLIENT) {
                                ClientView.clientDashboard(loggedInUser);
                            } else if (role == Role.BUILDER) {
                                BuilderView.builderDashboard(loggedInUser);
                            } else if (role == Role.PROJECT_MANAGER) {
                                ProjectManagerView.ProjectManagerDashboard(loggedInUser);
                            }
                        } catch (ValidationException e) {
                            logger.log(Level.SEVERE, "Login failed for user: " + loginUsername, e);
                            System.out.println("Login failed: " + e.getMessage());
                        }
                    }

                    case 3 -> {
                        System.out.println("\nThank you for using the Project Management System. Goodbye!");
                        logger.info("Application exited by user.");
                        running = false;
                    }

                    default -> {
                        System.out.println("\nInvalid choice. Please enter 1, 2, or 3.");
                        logger.warning("User entered invalid main menu choice: " + choice);
                    }
                }

            } catch (NoSuchElementException e) {
                running = false;
                logger.warning("Input error or stream closed: " + e.getMessage());
            } catch (ValidationException e) {
                System.out.println("\nInput Error: " + e.getMessage());
                logger.warning("Validation error: " + e.getMessage());
            }
        }

        sc.close();
    }
}
