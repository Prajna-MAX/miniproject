package org.zeta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeta.dao.BaseDao;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.*;
import org.zeta.service.implementation.ProjectManagerService;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class ProjectManagerTest {

    private ProjectDao projectDao;
    private TaskDao taskDao;
    private UserDao userDao;
    private BaseDao baseProjectDao;
    private User manager;
    private Project project;
    Task task;
    Builder builder;

    @BeforeEach
    void setUp() {
        projectDao = mock(ProjectDao.class);
        taskDao = mock(TaskDao.class);
        userDao = mock(UserDao.class);
        baseProjectDao = mock(BaseDao.class);
        manager = new User("manager1", "pass", Role.PROJECT_MANAGER);
        manager.setId("m1");
        project = new Project("p1", "Project One");
        project.setProjectManagerId(manager.getId());
        project.setStatus(ProjectStatus.InProgress);
        project.setClientId("c1");

    }

    @Test
    void listProjects_shouldCallGetAll() {
        when(baseProjectDao.getAll()).thenReturn(List.of(project));
        ProjectManagerService.listProjects(baseProjectDao, manager);
        verify(baseProjectDao).getAll();
    }

    @Test
    void listClients_shouldCallFindByRole() {
        ProjectManagerService.listClients(userDao);
        verify(userDao).findByRole(Role.CLIENT);
    }

    @Test
    void viewProjectsByClient_shouldPrintClientProjects() {
        User client = new User("client1", "pass", Role.CLIENT);
        client.setId("c1");
        when(userDao.findIdbyName("client1")).thenReturn(Optional.of(client));
        when(baseProjectDao.getAll()).thenReturn(List.of(project));
        ProjectManagerService.viewProjectsByClient("client1", userDao, baseProjectDao);
    }

    @Test
    void invalidClient_shouldReturnWhenClientNotFound() {
        when(userDao.findIdbyName("client1")).thenReturn(Optional.empty());
        ProjectManagerService.viewProjectsByClient("client1",userDao,baseProjectDao);
        verify(userDao).findIdbyName("client1");
    }

    @Test
    void assignTask_projectNotFound() {
        when(projectDao.findById("p1")).thenReturn(Optional.empty());
        ProjectManagerService.assignTask("p1", "p1", "p1", taskDao, userDao);
        verify(taskDao, times(0)).assignBuilder("p1", "p1");

    }
    @Test
    void assignTask_managerNotFound() {
        when(userDao.findById("p1")).thenReturn(Optional.empty());
        ProjectManagerService.assignTask("p1", "p1", "p1", taskDao, userDao);
        verify(taskDao, times(0)).assignBuilder("p1", "p1");

    }
    @Test
    void testValidBuilderAvailable() {
        task = new Task();
        builder = new Builder();
        builder.setId("5L");
        when(taskDao.findByProjectId("p1")).thenReturn(List.of(task));
        when(userDao.findByRole(Role.BUILDER)).thenReturn(List.of(builder));
        ProjectManagerService.assignTask("p1", "1L", "5L", taskDao, userDao);
        verify(taskDao, times(1)).assignBuilder("1L", "5L");
    }

    @Test
    void testAddProjectDetails_projectNotFound() {
        when(projectDao.findById("p1")).thenReturn(Optional.empty());
        ProjectManagerService.addProjectDetails("p1","New",10,projectDao,manager);
        verify(projectDao,never()).update(any());
    }
    @Test
    void testValidBuilderAssignment() {
        Task task = new Task();
        task.setId("t1");
        task.setTaskName("Task 1");
        User builder = new User("builder1", "pass", Role.BUILDER);
        builder.setId("b1");
        when(taskDao.findByProjectId("p1")).thenReturn(List.of(task));
        when(userDao.findByRole(Role.BUILDER)).thenReturn(List.of(builder));
        ProjectManagerService.assignTask("p1", "t1", "b1", taskDao, userDao);
        verify(taskDao, times(1)).assignBuilder("t1", "b1");
    }
    @Test
    void testEmptyClientList() {
        when(userDao.findByRole(Role.CLIENT)).thenReturn(List.of());
        ProjectManagerService.listClients(userDao);
        verify(userDao, times(1)).findByRole(Role.CLIENT);
    }
    @Test
    void testClientList() {
        User client = new User("builder1", "pass", Role.CLIENT);
        when(userDao.findByRole(Role.CLIENT)).thenReturn(List.of(client));
        ProjectManagerService.listClients(userDao);
        verify(userDao, times(1)).findByRole(Role.CLIENT);
    }

    @Test
    void testcreateTask_projectNotFound(){
        when(projectDao.findById("p1")).thenReturn(Optional.empty());
        ProjectManagerService.createTask("p1","T1",taskDao,projectDao,manager);
        verify(taskDao, never()).add(any());
        verify(projectDao, times(1)).findById("p1");
    }
    @Test
    void testCreateTask_invalidManager() {
        Project project = new Project();
        project.setProjectId("p1");
        project.setProjectManagerId("MANAGER2");
        when(projectDao.findById("p1")).thenReturn(Optional.of(project));
        manager.setId("manager1");
        ProjectManagerService.createTask("p1", "T1", taskDao, projectDao, manager);
        verify(taskDao, never()).add(any());
    }

@Test
    void testAddTask(){
    when(projectDao.findById("p1")).thenReturn(Optional.of(project));
    ProjectManagerService.createTask("p1","T1",taskDao,projectDao,manager);
    verify(projectDao, times(1)).findById("p1");
}


}
