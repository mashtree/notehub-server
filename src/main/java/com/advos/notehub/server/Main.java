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
        try{
            Registry server = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        
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
