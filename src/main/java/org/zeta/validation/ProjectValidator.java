package org.zeta.validation;

public class ProjectValidator {

    public static void validateProjectCreation(String projectName,
                                               String clientId) {

        CommonValidator.validateNotNullOrEmpty(projectName, "Project Name");
        CommonValidator.validateMinLength(projectName, 3, "Project Name");

        CommonValidator.validateNotNullOrEmpty(clientId, "Client ID");
        CommonValidator.validateUUID(clientId, "Client ID");
    }
}
