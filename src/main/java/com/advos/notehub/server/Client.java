/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advos.notehub.server;

import com.notehub.api.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author aisyahumar
 */
public class Client {
    
    public static final int MAGIC_VALUE = 32;
    
    /**
     * store client
     * key User object
     * value TimeStamp
     */
    public static final Map<Integer, Integer> clients = new HashMap<>();
    
    /**
     * requests
     * services' object
     * map (timestamp, request)
     */
    public static final Map<Object, TreeMap<Integer, Object>> requests = new HashMap<>();
    
    private static final List<Integer> idOnlineClients = new ArrayList<>();
    
    public static int randomIdOnline(User user){
        int res = (int) new Date().getTime() + (Client.MAGIC_VALUE*user.getIdUser());
        if(idOnlineClients.contains(res)){
            return randomIdOnline(user);
        }else{
            idOnlineClients.add(res);
        }
        System.out.println("res"+res);
        return res;
    }
    
}
