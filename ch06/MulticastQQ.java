/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author 高佬_
 */
public class MulticastQQ {
    InetAddress groupIP;// = InetAddress.getByName("224.0.1.8");
    int port = 8900;
    MulticastSocket ms = null; //组播套接字
    
    byte[] inBuff;
    byte[] outBuff;
    
    public MulticastQQ() throws IOException
    {
        groupIP = InetAddress.getByName("224.0.1.8");
        
        ms = new MulticastSocket(port); //开启一个组播端口(UDP端口)
        ms.joinGroup(groupIP); //告诉网卡这样的IP地址数据包要接收
        
        inBuff = new byte[1024];
        outBuff = new byte[1024];
    }
    
    public void send(String msg)
    {
        try
        {
            outBuff = ("20121000000老师 : " + msg).getBytes("GBK");
            DatagramPacket outdp = new DatagramPacket(outBuff, outBuff.length, groupIP, port);
            ms.send(outdp);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String receive()
    {
        try
        {
            DatagramPacket indp = new DatagramPacket(inBuff,inBuff.length);
            
            ms.receive(indp);
            String msg = new String(indp.getData(), 0, indp.getLength(), "GBK");
            return indp.getAddress() + "--->" + msg;
                
        }catch(Exception e)
        {
            return null;
           // e.printStackTrace();
        }
    }
    
    public void close()
    {
        try
        {
            ms.leaveGroup(groupIP);
            ms.close();
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
   
    
}
