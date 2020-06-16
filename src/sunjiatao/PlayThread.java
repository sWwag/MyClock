package sunjiatao;

import javax.swing.*;

public class PlayThread extends Thread {
    static boolean isRunning=false;
    @Override
    public void run() {
        isRunning=true;
        super.run();
        JOptionPane.showMessageDialog(null,"时间到了！！！","到时提醒",JOptionPane.INFORMATION_MESSAGE);
        PlayerManager.stop();
        isRunning=false;
    }
}
