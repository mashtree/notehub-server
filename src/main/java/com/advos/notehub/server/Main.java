/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server;

import com.advos.notehub.server.service.AuthServiceServer;
import com.advos.notehub.server.service.NoteChangeServiceServer;
import com.advos.notehub.server.service.NotesServiceServer;
import com.advos.notehub.server.service.UsersServiceServer;
import com.advos.notehub.server.util.Database;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author aisyahumar
 */
public class Main {
    
    public static void main(String[] args){
        //System.setSecurityManager(new RMISecurityManager());
        int port = args.length>0?Integer.parseInt(args[0]):Registry.REGISTRY_PORT;
        try{
            Registry server = LocateRegistry.createRegistry(port);
        
            //Database.getConnection();

            UsersServiceServer uss = new UsersServiceServer();

            server.rebind("UsersServiceServer", uss);

            NotesServiceServer nss = new NotesServiceServer();

            server.rebind("NoteServiceServer", nss);

            NoteChangeServiceServer ncss = new NoteChangeServiceServer();

            server.rebind("NoteChangeServiceServer", ncss);
            
            AuthServiceServer as = new AuthServiceServer();
            
            server.rebind("AuthServiceServer", as);

            System.out.println("Server is running at "+server.toString());
        }catch(RemoteException re){
            System.out.println("Server error : "+re);
        }
        
    }
    
}
