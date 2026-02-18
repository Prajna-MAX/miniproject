package org.zeta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.model.Project;
import org.zeta.model.Task;
import org.zeta.model.TaskStatus;
import org.zeta.model.User;
import org.zeta.service.implementation.BuilderService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuilderServiceTest {

    private TaskDao taskDao;
    private User builder;
    private List<Task> tasks;

    @BeforeEach
    void setup() {
        taskDao = mock(TaskDao.class);
        builder = new User();
        builder.setId("B1");
        Task t1 = new Task();
        t1.setId("T1");
        t1.setBuilderId("B1");
        t1.setTaskName("Task1");
        t1.setStatus(TaskStatus.NOT_STARTED);
        tasks = new ArrayList<>();
        tasks.add(t1);

        BuilderService.taskDao = taskDao;
        BuilderService.tasks = tasks;
    }

    @Test
    void updateStatus_TaskExistsForBuilder_ShouldMarkCompleted() {

        BuilderService.updateStatus("Task1", builder);

        Task updatedTask = tasks.get(0);

        assertEquals(TaskStatus.COMPLETED, updatedTask.getStatus());
        verify(taskDao, times(1)).saveTask(updatedTask);
    }

    @Test
    void updateStatus_TaskDoesNotExist_ShouldRemainNotStarted() {

        BuilderService.updateStatus("WrongTask", builder);
        Task task = tasks.get(0);
        assertEquals(TaskStatus.NOT_STARTED, task.getStatus());
        verify(taskDao, times(1)).saveTask(task);
    }

    @Test
    void updateStatus_TaskBelongsToAnotherBuilder_ShouldNotChange() {

        User otherBuilder = new User();
        otherBuilder.setId("B2");
        BuilderService.updateStatus("Task1", otherBuilder);
        Task task = tasks.get(0);
        assertEquals(TaskStatus.NOT_STARTED, task.getStatus());
        verify(taskDao, never()).saveTask(task);
    }

    @Test
    void listOfTasks_ShouldPrintProjectNameAndTaskName() {

        Project project = new Project();
        project.setProjectId("P1");
        project.setProjectName("ProjectAlpha");

        ProjectDao projectDao = mock(ProjectDao.class);
        when(projectDao.getAll()).thenReturn(List.of(project));

        Task task = new Task();
        task.setProjectId("P1");
        task.setTaskName("Task1");
        task.setBuilderId("B1");
        BuilderService.projectDao = projectDao;
        BuilderService.tasks = List.of(task);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));
        BuilderService.listOfTasks(builder);
        System.setOut(originalOut);
        assertTrue(out.toString().contains("ProjectAlpha - Task1"));
    }
}
