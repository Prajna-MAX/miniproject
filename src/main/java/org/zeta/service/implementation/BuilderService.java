package org.zeta.service.implementation;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.model.Project;
import org.zeta.model.Task;
import org.zeta.model.TaskStatus;
import org.zeta.model.User;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BuilderService {
    static Scanner sc = new Scanner(System.in);
    static TaskDao taskDao = new TaskDao();
    static ProjectDao projectDao = new ProjectDao();
    static List<Task> tasks = taskDao.getAll();

    public static void listOfTasks(User Builder) {
        List<Project> projects = projectDao.getAll();
        tasks.stream()
                .filter(t -> Objects.equals(t.getBuilderId(), Builder.getId()))
                .forEach(t -> {
                    Project project = projects.stream()
                            .filter(p -> Objects.equals(p.getProjectId(), t.getProjectId()))
                            .findFirst()
                            .orElse(null);

                    String projectName = project.getProjectName();

                    System.out.println(projectName + " - " + t.getTaskName());
                });
    }

    public static void updateStatus(String taskName, User Builder) {
        tasks.stream()
                .filter(t -> Objects.equals(t.getBuilderId(), Builder.getId()))
                .forEach(t -> {
                    if (t.getTaskName().equals(taskName)) {
                        t.setStatus(TaskStatus.COMPLETED);
                    }
                    taskDao.saveTask(t);

                });

    }

}
