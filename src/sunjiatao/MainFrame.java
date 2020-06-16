package sunjiatao;

import javazoom.jl.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


@SuppressWarnings("SpellCheckingInspection")
public class MainFrame extends JFrame
{
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 600;

    private JComboBox<String> hourBox=new JComboBox<>();
    private JComboBox<String> minBox=new JComboBox<>();
    private final JButton addClockButton =new JButton("添加闹钟");
    private final JButton deleteClockButton = new JButton("删除闹钟");
    private final JCheckBox checkBox =new JCheckBox("铃声提醒");

    private int h,m,s;
    private  int HOUR;
    private  int MIN;//下拉框中的时间
    private final String[][] str= new String[100][2];

    static Player player = null;
    private final String path ="D:\\MyClock\\时间记录.txt";
    private final String music ="D:\\MyClock\\大鱼.mp3";

    //ctor
    public MainFrame()
    {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("闹钟");
        setIcon();
        compositeControls();
        //getTime();
        setVisible(true);
        setLocation(500,200);

        Timer timer =new Timer(1000, e -> {
            getTime();
            s += 6;                // 秒针每次走6°
            if (s >= 360)
            {
                s = 0;            // 秒针归零
                m += 6;
                if (m == 72 || m == 144 || m == 288)
                {
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
            //paintClock(g);
            repaint();     // 重新绘制
            prompt();       //将闹钟加入到线程当中
            //System.out.println("111");
        });
        timer.start();
    }
    //设置图标
    private void setIcon()
    {
        try
        {
            Image img = ImageIO.read(this.getClass().getResource("闹钟.jpg"));
            this.setIconImage(img);
        }
        catch (IOException e)
        {
            //System.out.println("sjthhh");
        }
    }

    public void play()
    {
        new Thread(() ->
        {
            try {
                File file = new File(music);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream stream = new BufferedInputStream(fis);
                player = new Player(stream);
                player.play();
            }
            catch (Exception e)
            {
                // TODO: handle exception
            }
        }).start();
        new Thread(()->
        {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                player.close();
            }
        });
    }
    //弹窗提醒
    public void prompt()
   {
        Calendar now1 = new GregorianCalendar();
        int a, b;
        a = now1.get(Calendar.HOUR_OF_DAY);
        b = now1.get(Calendar.MINUTE);

        try (FileReader reader = new FileReader(path); BufferedReader br = new BufferedReader(reader))
        {
            String line;
            String[][] str= new String[100][2];
            int i =0;
            while ((line = br.readLine()) != null)
            {
                int j =0;
                StringTokenizer st = new StringTokenizer(line,":");
                while (st.hasMoreTokens())
                {
                    str[i][j]=st.nextToken();
                    j++;
                }
                if (a==Integer.parseInt(str[i][0]) && b==Integer.parseInt(str[i][1]))
                {
                    if(checkBox.isSelected())
                    {
                        play();
                        player.close();
                    }
                    JOptionPane.showMessageDialog(null,"时间到了！！！","到时提醒",JOptionPane.INFORMATION_MESSAGE);
                }
                i++;
                j = 0;
            }
        }
        catch (IOException z)
        {
            z.printStackTrace();
        }
    }
    //闹钟显现
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.black);
        Graphics2D g2=(Graphics2D)g;
        g2.setColor(Color.black);
        int x = 100;
        int y = 100;
        int r = 100;
        double rad = Math.PI / 180;
        g.drawOval(x, y, r * 2, r * 2);


        // 秒针
        int x1 = (int) (90 * Math.sin(rad * s));
        int y1 = (int) (90 * Math.cos(rad * s));
        g.drawLine(r+x, y+r, r+x + x1, r +y- y1);


        // 分针
        x1 = (int) (80 * Math.sin(rad * m));
        y1 = (int) (80 * Math.cos(rad * m));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(r+x, r+y, r +x+ x1, r+y - y1);

        // 时针
        x1 = (int) (60 * Math.sin(rad * h));
        y1 = (int) (60 * Math.cos(rad * h));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(r+x, r+y, r+x + x1, r +y- y1);

        // 画数字
        int d = 30;
        for (int i = 1; i <= 12; i++) {
            x1 = (int) ((r - 10) * Math.sin(rad * d));
            y1 = (int) ((r - 10) * Math.cos(rad * d));
            if(i<10) {
                g.drawString(String.valueOf(i), r + x + x1, r + y - y1);
                d += 30;
            }
            else
            {
                g.drawString(String.valueOf(i), r + x + x1, r + y - y1);
                d += 30;
            }
        }

        // 画刻度
        d = 0;
        for (int i = 0; i < 60; i++)
        {
            x1 = (int) ((r - 2) * Math.sin(rad * d));
            y1 = (int) ((r - 2) * Math.cos(rad * d));
            g2.setStroke(new BasicStroke(1.3f));
            if(i%5==0)
            {
                g2.drawLine(r + x + x1,r + y - y1, r+x+(int) (r  * Math.sin(rad * d)),r+y-(int) (r * Math.cos(rad * d)));
                d += 6;
            }
            else {
                g.drawString(".", r + x + x1, r + y - y1);
                d += 6;
            }
        }
        // 显示时间
        Calendar now1 = new GregorianCalendar();
        int a, b, c;
        a = now1.get(Calendar.HOUR_OF_DAY);
        b = now1.get(Calendar.MINUTE);
        c = now1.get(Calendar.SECOND);
        g.drawString(a+ ":" + b + ":" + c, 175, 330);
        g.drawString("全部闹钟：",100,350);

        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)
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
                i++;
                j = 0;
            }
        } catch (IOException z) {
            z.printStackTrace();
        }

    }

    //初始化
    public void getTime()
    {
        Calendar now =new GregorianCalendar();
        s = (now.get(Calendar.SECOND)-1)*6;
        m = now.get(Calendar.MINUTE) * 6;
        h = now.get(Calendar.HOUR) * 30 + now.get(Calendar.MINUTE) / 12 * 6;

    }
    //文件读取
    public void getClocksFromFile()
    {
        try(FileReader reader=new FileReader(path);
            BufferedReader bufferedReader =new BufferedReader(reader))
        {
            String line;
            int i=0;
            while((line=bufferedReader.readLine())!=null)
            {
                int j=0;
                StringTokenizer st = new StringTokenizer(line, ":");
                while (st.hasMoreTokens())
                {
                    str[i][j]=st.nextToken();
                    j++;
                }
                //System.out.print(str[i][0]+":"+str[i][1]);
                //System.out.println();
                i++;
                j=0;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //闹钟从txt写入
    public void writeClockToFile()
    {
        HOUR = hourBox.getSelectedIndex();
        MIN = minBox.getSelectedIndex();
        try(FileWriter writer =new FileWriter(path,true);
            BufferedWriter out =new BufferedWriter(writer))
        {
            out.write(String.format("%02d",HOUR) + ":" + String.format("%02d",MIN) + "\r\n");
            out.flush();
            System.out.println("had added");
            JOptionPane.showMessageDialog(null,"闹钟添加成功！","添加闹钟提醒",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    //闹钟从txt删除
    public void deleteClockFromFile()
    {
        HOUR = hourBox.getSelectedIndex();
        MIN = minBox.getSelectedIndex();
        try(FileWriter writer =new FileWriter(path);//没有append：true，表示重新写
            BufferedWriter out =new BufferedWriter(writer))
        {
            getClocksFromFile();
            for (int i = 0; i < 100; i++)
            {
                if (Integer.parseInt(str[i][0])==HOUR && Integer.parseInt(str[i][1])==MIN)
                {
                }
                else
                {
                    out.write(str[i][0]+":"+str[i][1]+"\r\n");
                }
            }
            out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException e)
        {
            System.out.println("had deleted");
            JOptionPane.showMessageDialog(null,"该闹钟已删除！","删除闹钟提醒",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //绘制控件
    public void compositeControls()
    {
        JPanel panel=new JPanel();
        this.getContentPane().add(panel);

        //设置小时下拉框
        String[] hours = new String[24];
        for (int i = 0; i < hours.length; i++)
        {
            hours[i]=i+"";
        }
        hourBox= new JComboBox<>(hours);
        hourBox.setSize(50,40);
        panel.add(hourBox);

        //设置分钟下拉框
        String[] mins = new String[60];
        for (int i = 0; i < mins.length; i++)
        {
            mins[i]=i+"";
        }
        minBox=new JComboBox<>(mins);
        minBox.setSize(50,40);
        panel.add(minBox);

        //添加按钮
        addClockButton.setSize(50,40);
        addClockButton.addActionListener(e -> {
            HOUR= hourBox.getSelectedIndex();
            MIN = minBox.getSelectedIndex();
            writeClockToFile();
            getClocksFromFile();
        });
        panel.add(addClockButton);

        //删除按钮
        deleteClockButton.setSize(50,40);
        deleteClockButton.addActionListener(e -> {
            deleteClockFromFile();
            getClocksFromFile();
        });
        panel.add(deleteClockButton);

        //铃声复选框
        panel.add(checkBox);
        panel.validate();
        this.add(panel);
    }

}