package sunjiatao;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class MainFrame extends JFrame  implements Runnable {
    //变量
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 600;
    private JPanel panel=new JPanel();
    private JComboBox hourBox=new JComboBox();
    private JComboBox minBox=new JComboBox() ;
    private  JButton addButton=new JButton("添加闹钟");
    private  JButton deleteButton= new JButton("删除闹钟");
    private  JCheckBox checkBox =new JCheckBox("铃声提醒");
    private int x=100;
    private int y=100;
    private int r=100;
    private int h,m,s;
    private  int HOUR;//
    private  int MIN;//下拉框中的时间
    private String[][] str= new String[100][2];
    private double rad=Math.PI/180;
    static Player player = null;
    private  String path ="D://MyClock//时间记录.txt";
    private  String music ="D://MyClock//大鱼.txt";
    public MainFrame(){
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("闹钟");
        unit();
        getTime();
        setVisible(true);
        setLocation(500,200);
    }
    //设置图标
    public  void setIcon()
    {
        try {
            Image img = ImageIO.read(this.getClass().getResource("闹钟.jpg"));
            this.setIconImage(img);
        } catch (IOException e) {
            System.out.println("sjthhh");
        }
    }

    //线程
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);// 间隔一秒
            } catch (Exception ex) {
            }
            s += 6;                // 秒针每次走6°
            if (s >= 360) {
                s = 0;            // 秒针归零
                m += 6;
                if (m == 72 || m == 144 || m == 288) {
                    h += 6;                // 分针走72°，时针走6° 分针的12倍，时针走一次
                }

                if (m >= 360) {
                    m = 0;                // 分针归零
                    h += 6;
                }
                if (h >= 360) {
                    h = 0;                // 时针归零
                }
            }
            this.repaint();     // 重新绘制
            this.prompt();       //将闹钟加入到线程当中
        }}

    //闹钟响起提示
    public void play() throws FileNotFoundException, JavaLayerException {

        BufferedInputStream buffer =
                new BufferedInputStream(new FileInputStream(music));
        player = new Player(buffer);
        player.play();
    }

   public void prompt(){
        Calendar now1 = new GregorianCalendar();
        int a, b;
        a = now1.get(Calendar.HOUR_OF_DAY);
        b = now1.get(Calendar.MINUTE);
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            String[][] str= new String[100][2];
            int i =0;
            while ((line = br.readLine()) != null) {
                int j =0;
                StringTokenizer st = new StringTokenizer(line, ":");
                while (st.hasMoreTokens()){
                    str[i][j]=st.nextToken();
                    j++;
                }
                if (a==Integer.valueOf(str[i][0]) && b==Integer.valueOf(str[i][1])){
                    if(checkBox.isSelected()) {
                        try {
                            File file = new File(music);
                            FileInputStream fis = new FileInputStream(file);
                            BufferedInputStream stream = new BufferedInputStream(fis);
                            Player player = new Player(stream);
                            player.play();
                            JOptionPane.showMessageDialog(null,"时间到了！！！","到时提醒",JOptionPane.INFORMATION_MESSAGE);
                        } catch (FileNotFoundException e) {
                            System.out.print("FileNotFoundException ");
                        } catch (JavaLayerException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"时间到了！！！","到时提醒",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                i++;
                j = 0;
            }
        } catch (IOException z) {
            z.printStackTrace();
        }
    }



    //闹钟显现

    public void paintClock(Graphics g)
    {
        super.paint(g);
        g.setColor(Color.BLACK);//画笔颜色
        g.drawOval(x, y, r * 2, r * 2);
        // 秒针
        int x1 = (int) (90 * Math.sin(rad * s));
        int y1 = (int) (90 * Math.cos(rad * s));
        g.drawLine(r+x, r+y, r+x + x1, r +y- y1);/* drawLine(a,b,c,d) (a,b)为起始坐标 (c,d)为终点坐标 */
        // 分针
        x1 = (int) (80 * Math.sin(rad * m));
        y1 = (int) (80 * Math.cos(rad * m));
        g.drawLine(r+x, r+y, r +x+ x1, r+y - y1);
        // 时针
        x1 = (int) (60 * Math.sin(rad * h));
        y1 = (int) (60 * Math.cos(rad * h));
        g.drawLine(r+x, r+y, r+x + x1, r +y- y1);
        // 画数字
        int d = 30;
        for (int i = 1; i <= 12; i++) {
            x1 = (int) ((r - 10) * Math.sin(rad * d));
            y1 = (int) ((r - 10) * Math.cos(rad * d));
            g.drawString(String.valueOf(i), r+x + x1, r+y - y1);    //字符型的数据才能画
        }
        // 画刻度
        d = 0;
        for (int i = 1; i <= 60; i++) {
            x1 = (int) ((r - 2) * Math.sin(rad * d));
            y1 = (int) ((r - 2) * Math.cos(rad * d));
            g.drawString(".", r+x + x1, r +y- y1);      //画的是点，表示刻度
            d += 6;
        }
        //获取时间
        Calendar now1 = new GregorianCalendar();
        int a,b,c;
        a=now1.get(Calendar.HOUR_OF_DAY);
        b = now1.get(Calendar.MINUTE);
        c = now1.get(Calendar.SECOND);
        g.drawString(a + ":" + b + ":" + c, 175, 330);
        g.drawString("全部闹钟：",100,350);
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;

            int i =0;
            while ((line = br.readLine()) != null) {
                int j =0;
                StringTokenizer st = new StringTokenizer(line, ":");
                while (st.hasMoreTokens()){
                    str[i][j]=st.nextToken();
                    j++;
                }
                g.drawString(str[i][0]+":"+str[i][1]+"\n",180+(i/10)*70,350+15*(i-(i/10)*10));  
                addButton.setText(str[i][0]+":"+str[i][1]+"\n");
                System.out.print(str[i][0]+":"+str[i][1]);
                System.out.println();
                i++;
                j = 0;
            }
        } catch (IOException z) {
            z.printStackTrace();
        }



    }

    //初始化
    public void getTime(){
        Calendar now =new GregorianCalendar();
        s=now.get(Calendar.HOUR_OF_DAY)*6;
        m = now.get(Calendar.MINUTE) * 6;
        h = now.get(Calendar.HOUR) * 30 + now.get(Calendar.MINUTE) / 12 * 6;
        Thread t=new Thread(this);
        t.start();
    }

    //文件读取
    public void readFile()
    {
        try(FileReader reader=new FileReader(path);
            BufferedReader bufferedReader =new BufferedReader(reader)
        ){
            String line;
            int i=0;
            while((line=bufferedReader.readLine())!=null){
                int j=0;
                StringTokenizer st = new StringTokenizer(line, ":");
                while (st.hasMoreTokens()){
                    str[i][j]=st.nextToken();
                    j++;
                }
                System.out.print(str[i][0]+":"+str[i][1]);
                System.out.println();
                i++;
                j=0;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //闹钟从txt写入
    public void writeFile()
    {
        HOUR = hourBox.getSelectedIndex();
        MIN = minBox.getSelectedIndex();
        try(FileWriter writer =new FileWriter(path,true);
            BufferedWriter out =new BufferedWriter(writer)
        ){
            out.write(HOUR + ":" + MIN + "\r\n");
            out.flush();
            System.out.println("had added");
            JOptionPane.showMessageDialog(null,"闹钟添加成功！","添加闹钟提醒",JOptionPane.INFORMATION_MESSAGE);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    //闹钟从txt删除
    public void deleteFile()
    {
        HOUR = hourBox.getSelectedIndex();
        MIN = minBox.getSelectedIndex();
        try(FileWriter writer =new FileWriter(path);//没有append：true，表示重新写
            BufferedWriter out =new BufferedWriter(writer)
        ){
            readFile();
            for (int i = 0; i < 100; i++) {
                if (Integer.valueOf(str[i][0])==HOUR && Integer.valueOf(str[i][1])==MIN){
                    continue;
                }
                else {
                    out.write(str[i][0]+":"+str[i][1]+"\r\n");
                }
            }
            out.flush();
        }catch (IOException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            System.out.println("had deleted");
            JOptionPane.showMessageDialog(null,"该闹钟已删除！","删除闹钟提醒",JOptionPane.INFORMATION_MESSAGE);

        }

    }
    //添加控件
    public void unit()
    {
        JPanel panel=new JPanel();
        this.getContentPane().add(panel);
        panel.validate();

        //设置小时下拉框
        String[] hours = new String[24];
        for (int i = 0; i < hours.length; i++) {
            hours[i]=i+"";
        }
        hourBox=new JComboBox(hours);
        hourBox.setSize(50,40);
        panel.add(hourBox);

        //设置分钟下拉框
        String[] mins = new String[60];
        for (int i = 0; i < mins.length; i++) {
            mins[i]=i+"";
        }
        minBox=new JComboBox(mins);
        minBox.setSize(50,40);
        panel.add(minBox);

        //添加按钮
        addButton.setSize(50,40);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HOUR= hourBox.getSelectedIndex();
                MIN = minBox.getSelectedIndex();
                writeFile();
                readFile();
            }
        });

        //删除按钮
        deleteButton.setSize(50,40);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFile();
                readFile();
            }
        });

        //铃声复选框
        panel.add(checkBox);
        panel.add(addButton);
        panel.add(deleteButton);
        this.add(panel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }


}
