/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server.util;

import com.advos.notehub.server.DBConf;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author triyono
 */
public class Database {

    private static Connection connection;

    public static Connection getConnection() {

        if (connection == null) {
            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                connection = DriverManager.getConnection("jdbc:mysql://"+DBConf.db_host+":"+DBConf.db_port+"/"+DBConf.db_name, DBConf.db_user, DBConf.db_pass);
                System.out.println("Connection to mysql database : "+DBConf.db_name+" is successfully created");
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return connection;
    }
}
