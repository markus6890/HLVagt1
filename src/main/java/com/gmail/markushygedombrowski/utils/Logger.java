package com.gmail.markushygedombrowski.utils;

import com.gmail.markushygedombrowski.HLvagt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    File file;
    private HLvagt plugin;
    public Logger(HLvagt plugin) {
        this.plugin = plugin;
        System.out.println(plugin.getDataFolder()+"");
        file = new File(plugin.getDataFolder() + "/reports.txt");
    }

    public void addMessage(String message){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(message);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setup(){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void formatMessage(String msg, String name){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
        String date = dateFormat.format(new Date());
        String finalMessage = date + " - "+ name + ": " + msg + "\n";
        addMessage(finalMessage);
    }
}
