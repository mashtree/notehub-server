/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.service;

import com.advos.notehub.server.util.Database;
import com.notehub.api.entity.Note;
import com.notehub.api.entity.NoteChange;
import com.notehub.api.entity.NoteChangesMap;
import com.notehub.api.service.NoteChangesService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aisyahumar
 */
public class NoteChangeServiceServer1 extends UnicastRemoteObject implements NoteChangesService{
    
    private Connection conn;
    private final String table = "note_change";
    
    public NoteChangeServiceServer1() throws RemoteException{
        if(conn==null) conn = Database.getConnection();
    }
    
    /*@Override
    public NoteChange insertNoteChange(NoteChange nc) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "INSERT INTO changes(id_user, id_note, row_change, old, new, created_at) "+
                " VALUES(?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, nc.getIdUser());
            ps.setInt(2, nc.getIdNote());
            ps.setInt(3, nc.getRowChange());
            ps.setString(4, nc.getOld());
            ps.setString(5, nc.getNewChanges());
            ps.setString(7, time);
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return nc;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
    
    @Override
    public void updateNoteChange(NoteChange nc) throws RemoteException {
        String sql = "UPDATE "+table+" SET id_user=?, id_note=?,row_change=?, old=?, new=?, created_at=? "+
                " where id_note_change = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, nc.getIdUser());
            ps.setInt(2, nc.getIdUser());
            ps.setInt(3, nc.getRowChange());
            ps.setString(4, nc.getOld());
            ps.setString(5, nc.getNewChanges());
            ps.setString(7, nc.getCreatedAt());
            ps.setInt(8,nc.getIdNoteChange());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void deleteNoteChanges(Note note) throws RemoteException {
        String sql = "DELETE FROM "+table+" WHERE id_note = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, note.getIdNote());
            ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public NoteChange getNoteChange(long l) throws RemoteException {
        NoteChange noteChange= null;
        String sql = "SELECT * FROM "+table+" where id_note_change=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                noteChange = new NoteChange();
                noteChange.setIdNoteChange(rs.getInt("id_note_change"));
                noteChange.setIdNote(rs.getInt("id_note"));
                noteChange.setIdUser(rs.getInt("id_user"));
                noteChange.setRowChange(rs.getInt("row_change"));
                noteChange.setOld(rs.getString("old"));
                noteChange.setNewChanges("new");
                noteChange.setCreatedAt(rs.getString("created_at"));
                
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return noteChange;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<NoteChange> getNoteChanges(long l) throws RemoteException {
        List<NoteChange> notechanges = new ArrayList<>();
        String sql = "SELECT * FROM "+table+" WHERE id_note=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                NoteChange noteChange= new NoteChange();
                noteChange.setIdNoteChange(rs.getInt("id_note_change"));
                noteChange.setIdNote(rs.getInt("id_note"));
                noteChange.setIdUser(rs.getInt("id_user"));
                noteChange.setRowChange(rs.getInt("row_change"));
                noteChange.setOld(rs.getString("old"));
                noteChange.setNewChanges("new");
                noteChange.setCreatedAt(rs.getString("created_at"));
                notechanges.add(noteChange);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return notechanges;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<NoteChange> getNoteChanges(Note note) throws RemoteException {
        return getNoteChanges(note.getIdNote());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NoteChangesMap insertNoteChange(NoteChangesMap ncm) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<NoteChange> getNoteChanges(Note note, int i) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLastVersion(Note note, int i) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<NoteChangesMap> getNoteChangesMap(Note note, int i, int i1) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
