package org.zeta.views;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Builder;
import org.zeta.model.Project;
import org.zeta.model.Task;
import org.zeta.model.User;
import org.zeta.service.implementation.BuilderService;
import org.zeta.service.implementation.ProjectManagerService;
import org.zeta.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BuilderView {
    static BuilderService builderService = new BuilderService();

    public static void builderDashboard(User builder) {

        UserDao userDao = new UserDao();
boolean running=true;
        Scanner sc = new Scanner(System.in);

        System.out.println("HI Builder");
        while (running) {
            System.out.println("""
                    \n===== Builder Dashboard =====
                    1. View my tasks
                    2.Update tasks
                    3. Logout
                    Enter your choice:""");
            try {
                int builderChoice = sc.nextInt();
                switch (builderChoice) {
                    case 1:
                        System.out.println("\n--- Your Tasks ---");

                        BuilderService.listOfTasks(builder);
                        break;
                    case 2:
                        BuilderService.updateStatus(builder);


                }
            } catch (ValidationException ValidationException) {
                System.out.println("Error: " + ValidationException.getMessage());
            }
        }
    }
}
