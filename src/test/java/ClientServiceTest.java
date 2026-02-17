import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.zeta.dao.ProjectDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.Role;
import org.zeta.model.User;
import org.zeta.service.implementation.ClientService;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    private ProjectDao projectDao;
    private UserDao userDao;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        projectDao = new ProjectDao("test-projects.json");
        userDao = new UserDao();
        clientService = new ClientService(projectDao, userDao);
        new File("test-projects.json").delete();
    }


    @Test
    @DisplayName("Given valid manager, when submitting project, then project should be saved")
    void givenValidManager_whenSubmitProject_thenProjectIsSaved() {
        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);
        String clientId = UUID.randomUUID().toString();
        clientService.submitProject("TDD Project", clientId);
        List<Project> projects = projectDao.getAll();
        assertEquals(1, projects.size());
    }

    @Test
    @DisplayName("Given project submission, when project is saved, then project name should match input")
    void givenProjectSubmission_whenSaved_thenProjectNameShouldMatch() {
        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);
        String clientId = UUID.randomUUID().toString();
        clientService.submitProject("TDD Project", clientId);
        Project savedProject = projectDao.getAll().get(0);
        assertEquals("TDD Project", savedProject.getProjectName());
    }



    @Test
    @DisplayName("Given no project managers, when submitting project, then exception should be thrown")
    void givenNoManager_whenSubmitProject_thenThrowException() {
        String clientId = "client-123";
        assertThrows(RuntimeException.class, () -> clientService.submitProject("Invalid Project", clientId));
    }


    @Test
    @DisplayName("Given projects exist for client, when fetching projects, then only client's projects should be returned")
    void givenProjectsExist_whenGetClientProjects_thenReturnMatchingProjects() {
        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);
        String clientId1 = UUID.randomUUID().toString();
        String clientId2 = UUID.randomUUID().toString();
        clientService.submitProject("Project A", clientId1);
        clientService.submitProject("Project B", clientId1);
        clientService.submitProject("Project C", clientId2);
        List<Project> result =
                clientService.getClientProjects(clientId1);
        assertEquals(2, result.size());
    }


    @Test
    @DisplayName("Given no projects for client, when fetching projects, then empty list should be returned")
    void givenNoProjects_whenGetClientProjects_thenReturnEmptyList() {
        List<Project> result = clientService.getClientProjects("unknown-client");
        assertTrue(result.isEmpty());
    }
}
