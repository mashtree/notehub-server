/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.service;

import com.advos.notehub.server.Client;
import com.advos.notehub.server.util.Database;
import com.notehub.api.entity.User;
import com.notehub.api.service.UsersService;
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
import java.util.Random;

/**
 *
 * @author aisyahumar
 */
public class UsersServiceServer extends UnicastRemoteObject implements UsersService{
    
    private Connection conn;
    private final String table = "users";
    
    public UsersServiceServer() throws RemoteException{
        if(conn==null) conn = Database.getConnection();
    }
    
    public void testMessage(String msg){
        System.out.println(msg);
    }

    @Override
    public User insertUser(User user) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String now = dateFormat.format(date);
        String sql = "INSERT INTO "+table+"(id, name, password, last_connect, ip_address, updated_at) "+
                " VALUES(?,?,?,?,?,?)";
        Random random = new Random();
        int id = random.nextInt(1000000)+5997;
        List<Integer> allUsersId = getAllUsersId();
        while(allUsersId.contains(id)){
            id = random.nextInt(1000000)+5997;
        }
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            user.setIdUser(id);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getLastConnect());
            ps.setString(4, user.getIpAddress());
            ps.setString(5, now);
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return user;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String now = dateFormat.format(date);
        String sql = "UPDATE "+table+" SET name = ?, password =?, last_connect=?, ip_address=?, updated_at=? "+
                " where username = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getLastConnect());
            ps.setString(4, user.getIpAddress());
            ps.setString(5, now);
            ps.setString(6, user.getUsername());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteUser(long l) throws RemoteException {
        String sql = "DELETE FROM "+table+" WHERE id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ps.executeUpdate();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUser(long l) throws RemoteException {
        User user = null;
        String sql = "SELECT * FROM "+table+" where id_repo=?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, (int) l);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setUsername(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLastConnect(rs.getString("last_connect"));
                user.setIpAddress(rs.getString("ip_address"));
                user.setUpdatedAt(rs.getString("updated_at"));
                
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return user;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getUsers() throws RemoteException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM "+table+" ";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setUsername(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLastConnect(rs.getString("last_connect"));
                user.setIpAddress(rs.getString("ip_address"));
                user.setUpdatedAt(rs.getString("updated_at"));
                users.add(user);
                
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return users;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private List<Integer> getAllUsersId(){
        List<Integer> allUsersId = new ArrayList<>();
        String sql = "SELECT id "+
                "FROM user";
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                allUsersId.add(rs.getInt("id"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return allUsersId;
    }
}
