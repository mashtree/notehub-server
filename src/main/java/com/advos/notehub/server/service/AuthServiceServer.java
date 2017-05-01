/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.service;

import com.advos.notehub.server.Client;
import com.advos.notehub.server.util.Database;
import com.advos.notehub.server.util.DateUtil;
import com.notehub.api.entity.User;
import com.notehub.api.service.AuthService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author triyono
 */
public class AuthServiceServer extends UnicastRemoteObject implements AuthService{
    
    /**
     * 
     */
    private Connection conn;
    
    /**
     * 
     * @throws RemoteException 
     */
    public AuthServiceServer() throws RemoteException{
        if(conn==null) conn = Database.getConnection();
    }
    
    /**
     * put user/client in the global variable like enumeration 
     * @param user
     * @return
     * @throws RemoteException 
     */
    @Override
    public User login(User user) throws RemoteException {
        String sql = "SELECT COUNT(name) as isexist "+
                "FROM users WHERE name='"+user.getUsername()+"' AND plain_password='"+user.getPassword()+"'";
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                if(rs.getInt("isexist")!=1){
                    return user;
                }else{
                    user = getUpdatedUser(user);
                    System.out.println(DateUtil.getTimeNow()+" "+user.getUsername()+" login, id_user:"+Client.clients.get(user.getIdUser()));
                }        
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return user;
    }
    
    /**
     * 
     * @param user 
     */
    private void setLastOnline(User user){
        String sql = "UPDATE users SET last_connect =?, ip_address=?, updated_at=? "+
                " where id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getLastConnect());
            ps.setString(2, user.getIpAddress());
            ps.setString(3, user.getLastConnect());
            ps.setInt(4, user.getIdUser());
            ps.executeUpdate();
       
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
    private User getUpdatedUser(User user){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String last_online = dateFormat.format(date);
        String sql = "SELECT * "+
                "FROM users WHERE name='"+user.getUsername()+"' AND plain_password='"+user.getPassword()+"'";
        try{
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                user.setUsername(rs.getString("name"));
                user.setPassword(rs.getString("plain_password"));
                user.setLastConnect(last_online);
                user.setIdUser(rs.getInt("id"));
            }
            int idOnline = Client.randomIdOnline(user);
            Client.clients.put(user.getIdUser(), idOnline); //store login user in global variable
            user.setIdOnline(idOnline);
            setLastOnline(user);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return user;
    }
    
    @Override
    public User logout(User user){
        Client.clients.remove(user.getIdUser()); //remove global login data
        user.setIdOnline(0);
        System.out.println(DateUtil.getTimeNow()+" "+user.getUsername()+" logout");
        return user;
    }

    @Override
    public Boolean isLogin(User user) throws RemoteException {
        if(Client.clients.containsKey(user.getIdUser())){
            System.out.println(DateUtil.getTimeNow()+" user : "+user.getUsername()+", id_online : "+user.getIdOnline());
            if(Client.clients.get(user.getIdUser())==user.getIdOnline()){
                System.out.println(DateUtil.getTimeNow()+" "+user.getUsername()+" checks login");
                return true;
            }
        }
        return false;
    }
    
}
