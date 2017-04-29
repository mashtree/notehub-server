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
import java.util.Map;

/**
 *
 * @author aisyahumar
 */
public class NoteChangeServiceServer extends UnicastRemoteObject implements NoteChangesService{
    
    private Connection conn;
    private final String table = "note_change";
    
    public NoteChangeServiceServer() throws RemoteException{
        if(conn==null) conn = Database.getConnection();
    }

    @Override
    //public NoteChange insertNoteChange(NoteChange nc) throws RemoteException {
    public NoteChangesMap insertNoteChange(NoteChangesMap ncm) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        Map<Integer, NoteChange> noteChangesMap = ncm.getNoteChangesMap();
        
        try{
            for(int key:noteChangesMap.keySet()){
                NoteChange nc = noteChangesMap.get(key);
                String sql = "INSERT INTO "+table+"(version, id_user, id_note, change_type, row_change, old, new, created_at) "+
                " VALUES(?,?,?,?,?,?,?,?)";
                //System.out.println(sql);
                //System.out.println(nc.getVersion()+","+nc.getIdUser()+","+nc.getIdNote()+","+nc.getChangeType()+","+nc.getRowChange());
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, nc.getVersion());
                ps.setInt(2, nc.getIdUser());
                ps.setInt(3, nc.getIdNote());
                ps.setString(4, nc.getChangeType());
                ps.setInt(5, nc.getRowChange());
                ps.setString(6, nc.getOld());
                ps.setString(7, nc.getNewChanges());
                ps.setString(8, time);
                
                ps.executeUpdate();
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println(time+" insert changes "+" "+noteChangesMap.size());
        return ncm;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void updateNoteChange(NoteChange nc) throws RemoteException {
        String sql = "UPDATE "+table+" SET id_user=?, id_note=?,row_change=?, old=?, new=?, created_at=?, change_type=? "+
                " where id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, nc.getIdUser());
            ps.setInt(2, nc.getIdUser());
            ps.setInt(3, nc.getRowChange());
            ps.setString(4, nc.getOld());
            ps.setString(5, nc.getNewChanges());
            ps.setString(7, nc.getCreatedAt());
            ps.setString(8, nc.getChangeType());
            ps.setInt(9,nc.getIdNoteChange());
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
        String sql = "SELECT * FROM "+table+" where id=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                noteChange = new NoteChange();
                noteChange.setIdNoteChange(rs.getInt("id"));
                noteChange.setIdNote(rs.getInt("id_note"));
                noteChange.setIdUser(rs.getInt("id_user"));
                noteChange.setRowChange(rs.getInt("row_change"));
                noteChange.setOld(rs.getString("old"));
                noteChange.setNewChanges("new");
                noteChange.setChangeType(rs.getString("change_type"));
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
                noteChange.setIdNoteChange(rs.getInt("id"));
                noteChange.setIdNote(rs.getInt("id_note"));
                noteChange.setIdUser(rs.getInt("id_user"));
                noteChange.setRowChange(rs.getInt("row_change"));
                noteChange.setOld(rs.getString("old"));
                noteChange.setNewChanges("new");
                noteChange.setChangeType(rs.getString("change_type"));
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

    /*@Override
    public NoteChangesMap insertNoteChange(NoteChangesMap ncm) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    @Override
    public List<NoteChange> getNoteChanges(Note note, int UID) throws RemoteException {
        return null;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLastVersion(Note note, int UID) throws RemoteException {
        String sql = "SELECT MAX(a.version) as version FROM "+table+" a LEFT JOIN users_to_notes b"
                + " ON a.id_note = b.id_note "
                + " WHERE b.id_note=? AND b.id_user=?";
        //System.out.println(sql+"-"+note.getIdNote()+"-"+UID);
        int version = 0;
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, note.getIdNote());
            ps.setInt(2, UID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                version = rs.getInt("version");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("last version - "+note.getIdNote()+"-"+note.getNoteTitle()+"-"+version);
        return version;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<NoteChangesMap> getNoteChangesMap(Note note, int id_note, int startingVersion) throws RemoteException {
        List<NoteChangesMap> result = new ArrayList<>();
        String sql = "SELECT * FROM "+table+" a LEFT JOIN users_to_notes b "
                + " ON a.id_note = b.id_note "
                + " WHERE a.version>="+startingVersion+" AND a.id_note="+note.getIdNote()+" "
                + " ORDER BY a.version ASC";
        System.out.println(" "+startingVersion+" "+note.getIdNote());
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setInt(1, startingVersion);
            //ps.setInt(2, note.getIdNote());
            ResultSet rs = ps.executeQuery();
            int version = 0;
            NoteChangesMap ncm = null;
            
            while(rs.next()){
                
                int rsVersion = rs.getInt("version");
                
                if(version!=rsVersion){
                    version = rsVersion;
                    System.out.println(rsVersion);
                    if(ncm!=null){
                        result.add(ncm);
                    }
                    ncm = new NoteChangesMap();
                    ncm.setVersion(version);
                    
                }
                NoteChange noteChange= new NoteChange();
                noteChange.setIdNoteChange(rs.getInt("id"));
                noteChange.setIdNote(rs.getInt("id_note"));
                noteChange.setIdUser(rs.getInt("id_user"));
                noteChange.setRowChange(rs.getInt("row_change"));
                noteChange.setOld(rs.getString("old"));
                noteChange.setNewChanges(rs.getString("new"));
                noteChange.setChangeType(rs.getString("change_type"));
                noteChange.setCreatedAt(rs.getString("created_at"));
                noteChange.setVersion(version);
                ncm.setNoteChange(noteChange.getIdNoteChange(), noteChange);
            }
            result.add(ncm);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return result;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
}
