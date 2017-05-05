/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.service;

import com.advos.notehub.server.util.Database;
import com.advos.notehub.server.util.DateUtil;
import com.notehub.api.entity.Note;
import com.notehub.api.entity.NoteChange;
import com.notehub.api.entity.NoteChangesMap;
import com.notehub.api.service.MessageClient;
import com.notehub.api.service.MessageServerService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author triyono
 */
public class MessageServerServiceServer extends UnicastRemoteObject implements MessageServerService {

    private static final long serialVersionUID = 1L;
    private Map<Integer, MessageClient> messageClients;
    private Connection conn;
    
    public MessageServerServiceServer() throws RemoteException {
        if(conn==null) conn = Database.getConnection();
        messageClients = new HashMap<>();
        
    }
    
    @Override
    public void removeRegisterUser(int key){
        if(messageClients.containsKey(key)){
            messageClients.remove(key);
            System.out.println(DateUtil.getTimeNow()+" remove clients "+key);
        }
    }

    @Override
    public void registerUser(int UID, MessageClient mc) throws RemoteException {
        messageClients.put(UID, mc);
        System.out.println(DateUtil.getTimeNow()+" add clients "+UID);
    }

    @Override
    public void broadcastMessage(String string, int noteID) throws RemoteException {
        System.out.println(DateUtil.getTimeNow()+" broadcast note's changes data");
        List<NoteChangesMap> listChange = getNoteChangesMap(noteID);
        Note note = getNote(noteID);
        for(int key:messageClients.keySet())
            messageClients.get(key).retrieveNoteChanges(note, listChange);
        
    }

    @Override
    public void broadcastNoteChanges(Note note, NoteChangesMap ncm) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private List<NoteChangesMap> getNoteChangesMap(int id_note) throws RemoteException {
        List<NoteChangesMap> result = new ArrayList<>();
        String sql = "SELECT * FROM note_change a LEFT JOIN users_to_notes b "
                + " ON a.id_note = b.id_note "
                + " WHERE a.version>=(SELECT MAX(version) FROM note_change WHERE id_note="+id_note+") AND a.id_note="+id_note+" "
                + " ORDER BY a.version ASC";
        //System.out.println(sql);
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
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
    }

    public Note getNote(long l) throws RemoteException {
        Note note= null;
        String sql = "SELECT * FROM notes where id=?";
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
    }
    

}
