/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author 高佬_
 */
public class GroupServer {
    
    private int port = 8008;
    private ServerSocket serverSocket = null;
    private ExecutorService executorService = null; //线程池
    private final int POOL_SIZE = 15; //单个CPU时线程池中工作线程的数目
    private static Set<Socket> Members;
    
    public GroupServer() throws IOException{
        serverSocket = new ServerSocket(port); //打开服务器端口
        Members = new HashSet<Socket>();
        //创建线程池
        //Runtime的availableProcessors()方法返回当前系统的CPU的数目
        //系统的CPU越多，线程池中工作线程的数目也应该越多
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        System.out.println("多用户服务器启动");
        
    }
    
    public void service(){
        while(true){
            Socket socket = null;
            try{
                socket = serverSocket.accept(); //监听客户请求, 阻塞语句.
                //接受一个客户请求,从线程池中拿出一个线程专门处理该客户.
                Members.add(socket);
                executorService.execute(new Handler(socket));
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    
    //在GroupServer主类中定义群组消息发送方法。
    public static void sendToAllMembers(String msg)throws IOException{
        PrintWriter pw;
        Socket tempSocket;
        OutputStream out;
        
        Iterator it = Members.iterator();
        while(it.hasNext()){
            tempSocket = (Socket) it.next(); //取出一个成员
            out = tempSocket.getOutputStream();
            pw = new PrintWriter(new OutputStreamWriter(out,"GB2312"),true);
            pw.println(msg);//发送聊天信息给成员
        }
    }
    public static void removeMember(Socket socket){
        Members.remove(socket);//删除一个成员
        
    }
   
    public static void main(String atgs[]) throws IOException{
        new GroupServer().service();
    }


    //定义内部类，实现线程接口
    class Handler implements Runnable{
        private Socket socket;

        public Handler(Socket socket){
            this.socket = socket;
        }

        private PrintWriter getWriter(Socket socket) throws IOException{
            OutputStream socketOut = socket.getOutputStream();
            return new PrintWriter(new OutputStreamWriter(socketOut,"GB2312"),true);
        }

        private BufferedReader getReader(Socket socket)throws IOException{
            InputStream socketIn = socket.getInputStream();
            return new BufferedReader(new InputStreamReader(socketIn,"GB2312"));
        }

        @Override
        public void run() {
            try{
                System.out.println("New connection accepted " +socket.getInetAddress());
                BufferedReader br =getReader(socket);//字节装饰成字符流
                PrintWriter pw = getWriter(socket); //字节装饰成字符流
                String msg = null;
                while ((msg = br.readLine()) != null) {
                    sendToAllMembers(msg);
                    if (msg.contains("bye".subSequence(0, 2))){
                        System.out.println( socket.getInetAddress() + ":" +"Exit");
                        break;
                    }
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }finally {
                try{
                    if(socket!=null)socket.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }

    }
}
