package org.zeta.model;

public class ProjectManager extends User {

    public ProjectManager(String username, String password) {
        super(username, password, Role.PROJECT_MANAGER);
    }
}

