/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.service;

import com.notehub.api.entity.NoteChange;
import com.notehub.api.service.NoteChangeService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aisyahumar
 */
public class NoteChangeServiceServer extends UnicastRemoteObject implements NoteChangeService{
    
    public NoteChangeServiceServer() throws RemoteException{
        
    }

    @Override
    public NoteChange insertNoteChange(NoteChange nc) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateNoteChange(NoteChange nc) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NoteChange getNoteChange(long l) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<NoteChange> getNoteChanges(long l) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
