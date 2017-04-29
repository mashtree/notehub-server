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
import java.sql.Statement;
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
        int last_inserted_id = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "INSERT INTO "+table+ "(note_title, description, owner, content, institution, institution_address, created_at)"
                + " VALUES(?,?,?,?,?,?,?)";
        //System.out.println(sql);
        try{
            PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, note.getNoteTitle());
            ps.setString(2, note.getDescription());
            ps.setInt(3, note.getOwner());
            ps.setString(4, note.getContent());
            ps.setString(5, note.getInstitution());
            ps.setString(6, note.getInstitutionAddress());
            ps.setString(7,time);
            ps.execute();
            System.out.println("create note "+note.getNoteTitle()+" time:"+time);
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
            {
                last_inserted_id = rs.getInt(1);
            }
            //insert
            note.setIdNote(last_inserted_id);
            //System.out.println(last_inserted_id);
            insertUsersToNotes(note.getOwner(),last_inserted_id,1);
        }catch(SQLException e){
            System.out.println("SQLException "+e);
        }
        
        return note;
    }
    
    /**
     * 
     * @param id_user
     * @param id_note
     * @param role 
     */
    private void insertUsersToNotes(int id_user, int id_note, int role){
        String sql = "INSERT INTO users_to_notes(id_user,id_note,role) VALUES(?,?,?)";
        //System.out.println(sql);
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_user);
            ps.setInt(2, id_note);
            ps.setInt(3, role);
            ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateNote(Note note) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        String sql = "UPDATE "+table+" SET note_title=?,description=?, owner=?, content=?, institution=?,institution_address=?, updated_at=?"+
                " where id = ?";
        //System.out.println(sql+"-"+note.getIdNote()+"-time:"+time);
        System.out.println("update "+note.getIdNote()+" "+note.getNoteTitle()+" time:"+time);
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, note.getNoteTitle());
            ps.setString(2, note.getDescription());
            ps.setInt(3, note.getOwner());
            ps.setString(4, note.getContent());
            ps.setString(5, note.getInstitution());
            ps.setString(6, note.getInstitutionAddress());
            ps.setString(7, time);
            ps.setInt(8, note.getIdNote());
            
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteNote(int i) throws RemoteException {
        //delete dulu link user->note
        String sql = "DELETE FROM "+table+" WHERE id = ?";
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
        String sql = "DELETE FROM "+table+" WHERE id = ?";
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
        String sql = "SELECT * FROM "+table+" where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                note = new Note();
                note.setIdNote(rs.getInt("id"));
                note.setNoteTitle(rs.getString("note_title"));
                note.setDescription(rs.getString("description"));
                note.setOwner(rs.getInt("owner"));
                note.setContent(rs.getString("content"));
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
                note.setIdNote(rs.getInt("id"));
                note.setNoteTitle(rs.getString("note_title"));
                note.setDescription(rs.getString("description"));
                note.setOwner(rs.getInt("owner"));
                note.setContent(rs.getString("content"));
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

    @Override
    public int isAvailable(int UID, String note) throws RemoteException {
        int result = 0;
        String sql = "SELECT count(a.id_users_to_note) as idCount FROM users_to_notes a"
                + " LEFT JOIN notes b ON a.id_note=b.id where a.id_user=? AND b.note_title=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, UID);
            ps.setString(2, note);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int tmp = (int) rs.getInt("idCount");
                if(tmp>result) result = tmp;
                
            }
        }catch(SQLException e){
            System.out.println("SQLException : "+e.getMessage());
        }
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
