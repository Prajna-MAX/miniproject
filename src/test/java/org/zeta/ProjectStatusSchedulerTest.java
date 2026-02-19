package org.zeta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.model.Project;
import org.zeta.model.Task;
import org.zeta.model.enums.ProjectStatus;
import org.zeta.model.enums.TaskStatus;
import org.zeta.scheduler.ProjectStatusScheduler;

import java.util.List;

import static org.mockito.Mockito.*;

class ProjectStatusSchedulerTest {

    private ProjectDao projectDao;
    private TaskDao taskDao;
    private ProjectStatusScheduler scheduler;

    @BeforeEach
    void setup() {
        projectDao = mock(ProjectDao.class);
        taskDao = mock(TaskDao.class);
        scheduler = new ProjectStatusScheduler(projectDao, taskDao);
    }

    @Test
    void shouldMarkProjectCompletedWhenAllTasksCompleted() throws InterruptedException {

        Project project = new Project();
        project.setProjectId("p1");
        project.setProjectName("Test Project");
        project.setStatus(ProjectStatus.InProgress);

        Task task1 = new Task();
        task1.setStatus(TaskStatus.COMPLETED);

        Task task2 = new Task();
        task2.setStatus(TaskStatus.COMPLETED);

        when(projectDao.getAll()).thenReturn(List.of(project));
        when(taskDao.findByProjectId("p1")).thenReturn(List.of(task1, task2));

        scheduler.start();
        Thread.sleep(1500);
        scheduler.stop();

        verify(projectDao, atLeastOnce()).update(project);
    }

    @Test
    void shouldNotUpdateWhenTasksNotCompleted() throws InterruptedException {

        Project project = new Project();
        project.setProjectId("p2");
        project.setStatus(ProjectStatus.InProgress);

        Task task = new Task();
        task.setStatus(TaskStatus.IN_PROGRESS);

        when(projectDao.getAll()).thenReturn(List.of(project));
        when(taskDao.findByProjectId("p2")).thenReturn(List.of(task));

        scheduler.start();
        Thread.sleep(1500);
        scheduler.stop();

        verify(projectDao, never()).update(project);
    }

    @Test
    void shouldSkipProjectWhenStatusNotInProgress() throws InterruptedException {

        Project project = new Project();
        project.setProjectId("p3");
        project.setStatus(ProjectStatus.Completed);

        when(projectDao.getAll()).thenReturn(List.of(project));

        scheduler.start();
        Thread.sleep(1500);
        scheduler.stop();

        verify(taskDao, never()).findByProjectId(any());
        verify(projectDao, never()).update(any());
    }

    @Test
    void shouldSkipWhenNoTasksFound() throws InterruptedException {

        Project project = new Project();
        project.setProjectId("p4");
        project.setStatus(ProjectStatus.InProgress);

        when(projectDao.getAll()).thenReturn(List.of(project));
        when(taskDao.findByProjectId("p4")).thenReturn(List.of());

        scheduler.start();
        Thread.sleep(1500);
        scheduler.stop();

        verify(projectDao, never()).update(project);
    }

    @Test
    void shouldShutdownScheduler() {
        scheduler.stop();
        // no exception = pass
    }
}
