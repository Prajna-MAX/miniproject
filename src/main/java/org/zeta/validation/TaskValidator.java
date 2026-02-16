package org.zeta.validation;

public class TaskValidator {

    public static void validateTaskCreation(String projectId,
                                            String taskName) {

        CommonValidator.validateNotNullOrEmpty(projectId, "Project ID");
        CommonValidator.validateUUID(projectId, "Project ID");

        CommonValidator.validateNotNullOrEmpty(taskName, "Task Name");
        CommonValidator.validateMinLength(taskName, 3, "Task Name");
    }

    public static void validateAssignment(String taskId,
                                          String builderId) {

        CommonValidator.validateNotNullOrEmpty(taskId, "Task ID");
        CommonValidator.validateNotNullOrEmpty(builderId, "Builder ID");

        CommonValidator.validateUUID(taskId, "Task ID");
        CommonValidator.validateUUID(builderId, "Builder ID");
    }
}
