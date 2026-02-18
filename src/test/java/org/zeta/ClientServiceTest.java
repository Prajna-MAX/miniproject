package org.zeta;

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

        assertThrows(
                RuntimeException.class,
                () -> clientService.submitProject("Invalid Project", clientId)
        );
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

        List<Project> result = clientService.getClientProjects(clientId1);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Given no projects for client, when fetching projects, then empty list should be returned")
    void givenNoProjects_whenGetClientProjects_thenReturnEmptyList() {
        List<Project> result = clientService.getClientProjects("unknown-client");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given empty project name, when submitting project, then ValidationException should be thrown")
    void givenEmptyProjectName_whenSubmitProject_thenThrowValidationException() {

        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);

        String clientId = UUID.randomUUID().toString();

        assertThrows(RuntimeException.class, () ->
                clientService.submitProject("", clientId)
        );
    }

    @Test
    @DisplayName("Given short project name, when submitting project, then ValidationException should be thrown")
    void givenShortProjectName_whenSubmitProject_thenThrowValidationException() {

        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);

        String clientId = UUID.randomUUID().toString();

        assertThrows(RuntimeException.class, () ->
                clientService.submitProject("AB", clientId)
        );
    }

    @Test
    @DisplayName("Given empty client ID, when submitting project, then ValidationException should be thrown")
    void givenEmptyClientId_whenSubmitProject_thenThrowValidationException() {

        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);

        assertThrows(RuntimeException.class, () ->
                clientService.submitProject("Valid Project", "")
        );
    }

    @Test
    @DisplayName("Given invalid UUID client ID, when submitting project, then ValidationException should be thrown")
    void givenInvalidUuid_whenSubmitProject_thenThrowValidationException() {

        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);

        assertThrows(RuntimeException.class, () ->
                clientService.submitProject("Valid Project", "not-a-uuid")
        );
    }
    @Test
    @DisplayName("Given null project name, when submitting project, then ValidationException should be thrown")
    void givenNullProjectName_whenSubmitProject_thenThrowValidationException() {

        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);

        String clientId = UUID.randomUUID().toString();

        assertThrows(RuntimeException.class, () ->
                clientService.submitProject(null, clientId)
        );
    }
    @Test
    @DisplayName("Given null client ID, when submitting project, then ValidationException should be thrown")
    void givenNullClientId_whenSubmitProject_thenThrowValidationException() {

        User manager = new User("pm1", "pass123", Role.PROJECT_MANAGER);
        userDao.save(manager);

        assertThrows(RuntimeException.class, () ->
                clientService.submitProject("Valid Project", null)
        );
    }


}
