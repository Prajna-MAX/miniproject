package org.zeta.scheduler;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.model.Project;
import org.zeta.model.Task;
import org.zeta.model.enums.ProjectStatus;
import org.zeta.model.enums.TaskStatus;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProjectStatusScheduler {

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private final ProjectDao projectDao;
    private final TaskDao taskDao;

    public ProjectStatusScheduler(ProjectDao projectDao, TaskDao taskDao) {
        this.projectDao = projectDao;
        this.taskDao = taskDao;
    }

    public void start() {

        scheduler.scheduleAtFixedRate(() -> {

            System.out.println("\n[Scheduler Running...] Checking project statuses...");

            List<Project> projects = projectDao.getAll();

            for (Project project : projects) {

                if (project.getStatus() != ProjectStatus.InProgress) {
                    continue;
                }

                List<Task> tasks =
                        taskDao.findByProjectId(project.getProjectId());

                if (tasks.isEmpty()) {
                    continue;
                }

                boolean allCompleted = tasks.stream()
                        .allMatch(task ->
                                task.getStatus() == TaskStatus.COMPLETED
                        );

                if (allCompleted) {
                    project.setStatus(ProjectStatus.Completed);
                    projectDao.update(project);

                    System.out.println("Project marked as COMPLETED: "
                            + project.getProjectName());
                }
            }

        }, 0, 10, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduler.shutdown();
    }
}
