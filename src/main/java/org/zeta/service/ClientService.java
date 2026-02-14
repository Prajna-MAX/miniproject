package org.zeta.service;

import org.zeta.dao.ProjectDao;
import org.zeta.model.Project;

public class ClientService {

    public void projectSubmit(String projectName,String clientId){
        Project project=new Project(projectName,clientId);
        ProjectDao projectDao=new ProjectDao();
        projectDao.saveProject(project);
        System.out.println("Project with "+project.getProjectName()+" submitted");

    }
}
