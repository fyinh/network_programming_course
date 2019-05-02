/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch04;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//文件数据服务器程序，开启2020端口。
//主要功能：根据请求的文件名传送文件，接收文件
/**
 *
 * @author pengbitao
 */
public class FileDataServer {
    private  ServerSocket serverSocket;
    
    private ExecutorService executorService; //线程池
    private final int POOL_SIZE = 100; //最大线程数
    
    public FileDataServer()
    {
        //启动对话框服务，等待客户的连接。
        try
        {
            serverSocket = new ServerSocket(2020);//服务器开启2020端口进行对话监听
            executorService = Executors.newCachedThreadPool();
            
            System.out.println("多线程FTP服务器数据处理部分启动,在2020端口。");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void service()
    {
        while(true)
        {
            Socket socket = null;
            try
            {
                socket = serverSocket.accept();//等待FTP客户端的连接请求
                
                //一旦连接成功，从线程池中取一个用来处理连接
                executorService.execute(new FtpDataHandler(socket));
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            
        }
    }
    
    class FtpDataHandler implements Runnable{
        private Socket socket;
        public FtpDataHandler(Socket socket)
        {
            this.socket = socket;
        }
        
        
        
        public boolean downFileNameisOk(String downFileName)
        {
            //String fName1 = "C:\\work\\netbeansProjects\\InternetProgramming\\JavaInternetApp\\FtpServer";//给出下载目录路径
            String fName1 = "G:\\download\\";
            File dirFile = new File(fName1);
            String[]  s= dirFile.list(); //获取目录下的所有的子目录和文件名
             //首先判断是否输入了正确的文件名
             int n = 0;
            boolean downflag = false;
            while(n < s.length)
            {
                if(s[n].equalsIgnoreCase(downFileName))
                {//客户输入了正确的文件名，才开始下载
                    downflag = true;
                    break;
                }
                n++;
            } 
            return downflag;
        }
        
        
         public void run()//覆盖线程体 ,,用来读取文件目录，传输给客户端
         {
             //得到网络输入字节流地址，并装饰成网络输入字符流
                //服务器读信息
                try
                {
                    InputStream socketIn = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(socketIn,"GB2312"));
                    
                    String downFileName = br.readLine();
                    System.out.println("要下载的文件为： " + downFileName);
                    
                    //判断要下载的文件名是否正确，以及是否为空
                    if((downFileNameisOk(downFileName) == false)||(downFileName == null))
                    {//如果要下载的文件名不正确，直接关闭socket
                        socket.close();  
                        return;
                    }
                    
                    
                    
                    
                    
                    
                    
                   // downFileName = "C:\\work\\netbeansProjects\\InternetProgramming\\JavaInternetApp\\FtpServer" + "\\" + downFileName;
                   downFileName = "G:\\download" + "\\" + downFileName;
                    OutputStream socketOut = socket.getOutputStream();
                    //  PrintWriter pw = new PrintWriter(new OutputStreamWriter(socketOut));
                   // PrintWriter pw = new PrintWriter(new OutputStreamWriter(socketOut,"GB2312"),true);
                    
                    
                    FileInputStream fileIn = new FileInputStream(downFileName);//新建本地空文件
                    
                    
                    byte[] buff = new byte[1024];//用来缓存接收的字节数据
                    
                    int len = fileIn.read(buff);
                    while(len !=-1)
                    {
                        System.out.println("len is: "+ len);
                        socketOut.write(buff,0,len);
                        
                        socketOut.flush();
                        //pw.println(buff);
                        len = fileIn.read(buff);
                        System.out.println("len is: "+ len);
                        
                    }
                    if(socketOut!=null)
                    {
                        socketOut.close();//此时客户端的socket.read()会返回-1
                    }
                    
                    
                    
                    System.out.println("文件传输OK");
                    
                    
                    
                    
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
               
         }
    }
    
    public static void main(String args[])
    {
        new FileDataServer().service();
    }
}
