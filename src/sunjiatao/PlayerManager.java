package sunjiatao;

import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class PlayerManager {
    private static final String music ="D:\\MyClock\\大鱼.mp3";
    static Player player;
    static Thread thread=null;
    public static void play() {
        thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try
                {
                    File file = new File(music);
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream stream = new BufferedInputStream(fis);
                    player = new Player(stream);
                    player.play();
                }
                catch (Exception ex)
                {
                    // TODO: handle exception
                }
            }
        });
        thread.start();
        try
        {
            Thread.sleep(20000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        player.close();
    }

    public static void stop()
    {
        thread.stop();
    }
}
