/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.service;

import com.advos.notehub.server.util.Database;
import com.notehub.api.entity.Note;
import com.notehub.api.service.NotesService;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aisyahumar
 */
public class NotesServiceServer extends UnicastRemoteObject implements NotesService{
    
    private Connection conn;
    private final String table = "notes";
    
    public NotesServiceServer() throws RemoteException{
        if(conn==null) conn = Database.getConnection();
    }
    
    public NotesServiceServer(Connection conn) throws RemoteException{
        this.conn = conn;
    }

    @Override
    public Note insertNote(Note note) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "INSERT INTO "+table+ "(note_title, description, owner, institution, institution_address, created_at)"
                + " VALUES(?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, note.getNoteTitle());
            ps.setString(2, note.getDescription());
            ps.setInt(3, note.getOwner());
            ps.setString(4, note.getInstitution());
            ps.setString(5, note.getInstitutionAddress());
            ps.setString(6,time);
            ps.execute();
        }catch(SQLException e){
            System.out.println("SQLException "+e);
        }
        return note;
    }

    @Override
    public void updateNote(Note note) throws RemoteException {
        String sql = "UPDATE "+table+" SET note_title=?,description=?, owner=?, institution=?,institution_address=?"+
                " where id_note = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, note.getNoteTitle());
            ps.setString(2, note.getDescription());
            ps.setInt(3, note.getOwner());
            ps.setString(4, note.getInstitution());
            ps.setString(5, note.getInstitutionAddress());
            ps.setInt(6, note.getIdNote());
            
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteNote(int i) throws RemoteException {
        //delete dulu link user->note
        String sql = "DELETE FROM "+table+" WHERE id_note = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, i);
            ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void deleteNote(Note note) throws RemoteException {
        NoteChangeServiceServer ncss = new NoteChangeServiceServer();
        ncss.deleteNoteChanges(note);
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
    public Note getNote(long l) throws RemoteException {
        Note note= null;
        String sql = "SELECT * FROM "+table+" where id_note=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                note = new Note();
                note.setIdNote(rs.getInt("id_note"));
                note.setNoteTitle(rs.getString("note_title"));
                note.setDescription(rs.getString("description"));
                note.setOwner(rs.getInt("owner"));
                note.setInstitution(rs.getString("institution"));
                note.setInstitutionAddress("institution_address");
                note.setCreatedAt(rs.getString("created_at"));
                
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return note;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Note> getNotes() throws RemoteException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM "+table+" ";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Note note = new Note();
                note.setIdNote(rs.getInt("id_note"));
                note.setNoteTitle(rs.getString("note_title"));
                note.setDescription(rs.getString("description"));
                note.setOwner(rs.getInt("owner"));
                note.setInstitution(rs.getString("institution"));
                note.setInstitutionAddress("institution_address");
                note.setCreatedAt(rs.getString("created_at"));
                notes.add(note);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return notes;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
