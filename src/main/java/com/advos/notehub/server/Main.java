/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server;

import com.advos.notehub.server.service.UsersServiceServer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author aisyahumar
 */
public class Main {
    
    public static void main(String[] args) throws RemoteException{
        Registry server = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        
        UsersServiceServer nss = new UsersServiceServer();
        
        server.rebind("UsersServiceServer", nss);
        
        System.out.println("Server is running at "+server.toString());
    }
    
}
