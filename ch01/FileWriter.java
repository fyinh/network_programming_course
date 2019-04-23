/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

/**
 *
 * @author 高佬_
 */
public class FileWriter {
    private PrintWriter pw = null;
    
    public FileWriter() {
        // 使用SAVE AS文件对话框命名文件.
//        JFileChooser jfc = new JFileChooser();
//        jfc.setCurrentDirectory(new File("G:\\"));
//        jfc.showSaveDialog(null);
//        File fileName = jfc.getSelectedFile();
        
        // 或直接指定文件名及保存位置.
        File fileName = new File("G:\\talk1.txt");
        try{
            FileOutputStream fw = new FileOutputStream(fileName, true);
            pw = new PrintWriter(fw);
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }    
    }
    
    public void append(String msg){
        pw.println(msg);
    }
    
    public void close(){
        pw.close();
    }
}
