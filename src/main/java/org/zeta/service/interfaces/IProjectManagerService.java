package org.zeta.service.interfaces;

import org.zeta.dao.ProjectDao;
import org.zeta.dao.TaskDao;
import org.zeta.dao.UserDao;
import org.zeta.model.Project;
import org.zeta.model.Task;
import org.zeta.model.User;

import java.util.List;

public interface IProjectManagerService {


    Task createTask(String projectId,
                    String taskName,
                    TaskDao taskDao,
                    ProjectDao projectDao,
                    User manager);

    void assignTask(String projectId,
                    String taskId,
                    String builderId,
                    TaskDao taskDao,
                    UserDao userDao);

}
