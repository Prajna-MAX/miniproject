package org.zeta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.zeta.dao.BaseDao;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.*;
import org.zeta.service.implementation.ProjectManagerService;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
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
    void listProjects_shouldPrintProjectsManagedByUser() {
        Project p2 = new Project("p2", "c1");
        p2.setProjectManagerId(manager.getId());
        p2.setStatus(ProjectStatus.Upcoming);
        when(baseProjectDao.getAll()).thenReturn(List.of(project, p2));
        ProjectManagerService.listProjects(baseProjectDao, manager);

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

        ProjectManagerService.viewProjectsByClient("client1",userDao, baseProjectDao);

    }
    @Test
    void invalidClient_shouldReturnWhenClientNotFound() {

        when(userDao.findIdbyName("client1")).thenReturn(Optional.empty());

        ProjectManagerService.viewProjectsByClient(
                "client1",
                userDao,
                baseProjectDao
        );

        verify(userDao).findIdbyName("client1");
    }


@Test
void addProjectDetails_whenProjectNotFound() {

    when(projectDao.findById("p1")).thenReturn(Optional.empty());

    ProjectManagerService.addProjectDetails(
            "p1",
            "New Desc",
            projectDao,
            manager
    );

    verify(projectDao, never()).update(any());
}



}
