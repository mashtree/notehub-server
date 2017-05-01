/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server;

import com.advos.notehub.server.service.AuthServiceServer;
import com.advos.notehub.server.service.MessageServerServiceServer;
import com.advos.notehub.server.service.NoteChangeServiceServer;
import com.advos.notehub.server.service.NotesServiceServer;
import com.advos.notehub.server.service.UsersServiceServer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author triyono
 */
public class Main {
    
    public static void main(String[] args){
        //System.setSecurityManager(new RMISecurityManager());
        System.setProperty("java.rmi.server.hostname", "192.168.130.2");
        System.out.println("Database connection setting");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Database Host : ");
        DBConf.db_host = scanner.nextLine();
        System.out.print("Database Port : ");
        DBConf.db_port = Integer.parseInt(scanner.nextLine());
        System.out.print("Database Name : ");
        DBConf.db_name = scanner.nextLine();
        System.out.print("Database Username : ");
        DBConf.db_user = scanner.nextLine();
        System.out.print("Database Password : ");
        DBConf.db_pass = scanner.nextLine();
        int port = args.length>0?Integer.parseInt(args[0]):Registry.REGISTRY_PORT;
        try{
            Registry server = LocateRegistry.createRegistry(port);

            UsersServiceServer uss = new UsersServiceServer();

            server.rebind("UsersServiceServer", uss);

            NotesServiceServer nss = new NotesServiceServer();

            server.rebind("NoteServiceServer", nss);

            NoteChangeServiceServer ncss = new NoteChangeServiceServer();

            server.rebind("NoteChangeServiceServer", ncss);
            
            AuthServiceServer as = new AuthServiceServer();
            
            server.rebind("AuthServiceServer", as);
            
            MessageServerServiceServer msss = new MessageServerServiceServer();
            
            server.rebind("MessageServerServiceServer", msss);

            System.out.println("Server is running at "+server.toString());
        }catch(RemoteException re){
            System.out.println("Server error : "+re);
        }
        
    }
    
}
