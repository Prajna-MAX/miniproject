package org.zeta.views;

import org.zeta.dao.BaseDao;
import org.zeta.dao.ProjectDao;
import org.zeta.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProjectManagerView {
    public static void ProjectManagerDashboard() {
        Scanner sc = new Scanner(System.in);
        BaseDao<Project> project ;
        System.out.println("HI Project manager");
        System.out.println("""
                Enter 1 to see list of all submitted projects
                Enter 2 to see list of unassigned projects\s
                Enter 3 to view the list of clients\s
                Enter 4 to view list of projects submitted by particular client""");

        int choice = sc.nextInt();
        switch (choice) {
            case 1:
               project = new ProjectDao();
               List<Project> projects = project.getAll();
                for (Project p : projects) {
                    System.out.println(p.getProjectName());
                }
                break;
            case 2:
                project = new ProjectDao();
                List<Project> proj= project.getAll();
                for (Project p : proj) {
                    if(p.getBuilderId()==null) {
                        System.out.println(p.getProjectName());
                    }
                }

        }


    }
}
