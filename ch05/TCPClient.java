/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch05;
import java.io.*;
import java.net.*;
/**
 *
 * @author 高佬_
 */
public class TCPClient {
    //套接字设计
    private Socket socket = null;
    //定义字符输入流和输出流
    private PrintWriter pw ;
    private BufferedReader br;
    
    public TCPClient(String ip, String port) throws IOException
    {
        socket = new Socket(ip,Integer.parseInt(port));
        
        //主动向服务器发起连接，实现TCP的三次握手过程
        //如果不成功，则抛出错误信息，其错误信息交由调用者处理
        //如果成功，则做如下两件事情
        
        OutputStream socketOut = socket.getOutputStream();
        pw = new PrintWriter(new OutputStreamWriter(socketOut,"GB2312"),true);
        //得到网络输出字节流地址，并封装成网络输出字符流
        
        InputStream socketIn = socket.getInputStream();
        br = new BufferedReader(new InputStreamReader(socketIn,"GB2312"));
        //得到网络输入字节流地址，并封装成网络输入字符流
    }
    
    public void send(String msg)
    {
        pw.println(msg);//输出字符流，由Socket调用系统底层函数，经网卡发送字节流
    }
    
    public String receive()
    {
        String msg;
        try
        {
            msg = br.readLine();//从网络输入字符流中读信息，每次只能接受一行信息
            //如果不够一行（无行结束符），则该语句阻塞，直到条件满足，程序才往下运行
        }catch(Exception e)
        {
            msg = null;
            e.printStackTrace();
        }
        return msg;
    }
    
    public void close()
    {
        try
        {
            if(socket!=null)
            {
                socket.close();//关闭连接，实现4次握手断开
                        
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    //模块内测试与运行，需先行运行TCPServer
    public static void main(String args[]) throws IOException
    {
        TCPClient tc = new TCPClient("127.0.0.1","8008");
        tc.send("123456789");//发送一串字符
        System.out.println(tc.receive());//接收一行字符串并在屏幕上显示
        tc.close();
    }
   
    
}
