/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

/**
 *
 * @author 高佬_
 */
public class UDPServer {
    private final static int port=8008;
    
    public static void main(String args[])throws IOException {
        int port1 = port;
        byte[] buffer =  new byte[512];
        try{
            DatagramSocket server = new DatagramSocket(port1);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("aaa");
            while(true){
                System.out.println("aaa000");
                server.receive(packet);
                System.out.println("aaa1111");
                String s = new String(packet.getData(), packet.getOffset(), packet.getLength(),"GB2312");
                System.out.println("bbb");
                String studentID = "20161003329";
                String studentName = "付颖雯";
                s = "  " + studentID + "-" + studentName + "-" + new Date().toString() +"-"+ s ;
                byte[] outputData = s.getBytes("GB2312");
                DatagramPacket outputPacket = new DatagramPacket(outputData, outputData.length, packet.getAddress(),packet.getPort());
                server.send(outputPacket);
                System.out.println("ccc");
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
