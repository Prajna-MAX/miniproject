package org.zeta.views;

import java.util.Scanner;

public class ClientView {
    public static void clientDashboard(){
        Scanner sc=new Scanner(System.in);
        System.out.println("HEY Client!");
        System.out.println("Enter 1 if you want to submit a project\n2 if you want to view your project's update");
        int clientChoice=sc.nextInt();
        switch(clientChoice){
            case 1:

        }
    }
}
