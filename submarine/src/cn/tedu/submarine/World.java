package cn.tedu.submarine;
import com.sun.org.apache.regexp.internal.RE;

import javax.swing.*;
import java.awt.Graphics;
import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class World extends JPanel{
    public static final int WIDTH=641;
    public static final int HEIGHT=479;

    public static final int RUNNING = 0;//运行状态
    public static final int PAUSE = 1;//暂停状态
    public static final int GAME_OVER =2;//游戏结束状态
    private int state = RUNNING; //当前状态 默认为运行状态

    //如下这一堆对象就是窗口中所看到的
    private Battleship ship=new Battleship();//战舰对象
    private SeaObject[] submarines={}; //潜艇的数组
    private Mine[] mines={}; //水雷数组
    private Bomb[] bombs={}; //炸弹数组


    //生成潜艇(侦察、鱼雷、水雷)对象
    private SeaObject nextSubmarine(){
        Random rand=new Random();
        int type=rand.nextInt(20);
        if(type<10){
            return new ObserveSubmarine();
        }else if (type<16){
            return new TorpedoSubmarine();
        }else {
            return new MineSubmarine();
        }
    }

    int subEnterIndex=0;
    //潜艇(侦察潜艇、鱼雷潜艇、水雷潜艇)入场
    private void submarinesEnterAction(){  //每10毫秒走一次
        subEnterIndex++; //没10毫秒增1
        if (subEnterIndex%40==0){  //每400毫秒走一次
            SeaObject obj=nextSubmarine(); //获取潜艇对象
            submarines= Arrays.copyOf(submarines,submarines.length+1); //扩容
            submarines[submarines.length-1]=obj; //将obj添加到submarine的最后一个
        }
    }

    private int mineEnterIndex = 0;
    //水雷入场
    private void mineEnterAction(){  //每10毫秒走一次
        mineEnterIndex++;
        if (mineEnterIndex%100==0){
            for(int i=0;i<submarines.length;i++){
                if (submarines[i] instanceof MineSubmarine){
                    MineSubmarine ms=(MineSubmarine)submarines[i];
                    Mine obj=ms.shootMine();//获取水雷对象
                    mines=Arrays.copyOf(mines,mines.length+1);
                    mines[mines.length-1]=obj;
                }
            }
        }
    }
    //海洋对象移动
    private void moveAction(){  //每10毫秒走一次
        for (int i=0;i<submarines.length;i++){//遍历所有潜艇
            submarines[i].move();
        }
        for (int i=0;i<mines.length;i++){ //遍历所有水雷
            mines[i].move();
        }
        for (int i=0;i<bombs.length;i++){//遍历所有炸弹
            bombs[i].move();
        }
    }
    //删除越界对象
    private void outOfBoundsAction(){//每10毫秒走一次
        for (int i=0;i<submarines.length;i++){
            if (submarines[i].isOutBounds() || submarines[i].isDead()){  //若出界
                //保证缩的一定是出界的
                submarines[i]=submarines[submarines.length-1];
                submarines=Arrays.copyOf(submarines,submarines.length-1);
            }

        }
        for (int i=0;i<mines.length;i++){
            if (mines[i].isOutBounds() || mines[i].isDead()){
                //保证缩的一定是出界的
                mines[i]=mines[mines.length-1];
                mines=Arrays.copyOf(mines,mines.length-1);
            }
        }
        for (int i=0 ;i<bombs.length;i++){
            if (bombs[i].isOutBounds() || bombs[i].isDead()){
                //保证缩的一定是出界的
                bombs[i]=bombs[bombs.length-1];
                bombs=Arrays.copyOf(bombs,bombs.length-1);
            }
        }

    }
    //炸弹与潜艇碰撞
    private int score=0;
    private void bombBangAction(){//每10毫秒走一次
        for (int i=0;i<bombs.length;i++){//遍历所有炸弹
            Bomb b=bombs[i];
            for (int j=0;j<submarines.length;j++){//遍历所有潜艇
                SeaObject s=submarines[j];
                if (b.isLive() && s.isLive() && s.isHit(b)){
                    s.goDead();//潜艇去死
                    b.goDead();//炸弹去死

                    if (s instanceof EnemyScore){//若潜艇被撞为分
                        EnemyScore es=(EnemyScore)s;//将被撞潜艇强转为得分接口
                        score+=es.getScore();//玩家得分
                    }
                    if (s instanceof  EnemyLife){//若潜艇被撞为命
                        EnemyLife el=(EnemyLife)s;//将被撞潜艇强转为得命接口
                        int num=el.getLife();//获取命数
                        ship.addLife(num);//战舰得命
                    }
                }
            }
        }
    }

    //水雷与战舰碰撞
    private void mineBangAction(){
        for (int i=0;i<mines.length;i++){ // 遍历所有水雷
            Mine m=mines[i]; //获取每个水雷
            if (m.isLive() && ship.isLive() && m.isHit(ship)){//都活着并且撞上了
                m.goDead();
                ship.subtractLife();
            }
        }
    }

    //检查游戏结束
    private void checkGameOverAction(){
        if (ship.getLife()<=0){
            state=GAME_OVER;
        }
    }

    //启动程序的执行
    private void action(){
        //游戏一开始，先询问是否读取存档
        File file=new File("game.sav");
        if (file.exists()){//判断存档文件是否存在
            //若存档文件存在，则询问用户是否读取存档
            int  r= JOptionPane.showConfirmDialog(
                    World.this,
                    "是否读取存档"
            );
            if (r==JOptionPane.YES_OPTION){//如果用户点击的位“是”这个按钮
                //读取存档
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    GameInfo gameInfo = (GameInfo) ois.readObject();
                    ship=gameInfo.getShip();
                    submarines=gameInfo.getSubmarines();
                    mines=gameInfo.getMines();
                    bombs=gameInfo.getBombs();
                    subEnterIndex=gameInfo.subEnterIndex;
                    mineEnterIndex=gameInfo.getMineEnterIndex();
                    score=gameInfo.getScore();
                    ois.close();
                }catch (Exception e){}
            }
        }

        KeyAdapter k= new KeyAdapter() {
            @Override
            //重写keyPressed()键盘按下事件  keyReleased()按键抬起事件
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_P){
                   state = PAUSE;//将游戏暂停
                    //方法返回值表示用户在弹出窗口上点击的是那个按钮
                    int r = JOptionPane.showConfirmDialog(
                            World.this,//参数1：确认框在游戏窗口上弹出
                             "保存游戏吗？"    //参数2：确认框上的提示文字
                    );//在当前游戏窗口前弹出一个确认框
                    //判断用户点击的是“是”按钮才进行存档
                    if (r==JOptionPane.YES_NO_OPTION){
                        //将当前World中各项信息都传入到GameInfo
                        GameInfo gameInfo=new GameInfo(ship,submarines,mines,bombs,subEnterIndex, mineEnterIndex,score);

                        try {
                            FileOutputStream fos = new FileOutputStream("game.sav");
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(gameInfo);//将当前游戏所有数据写入文件保存
                            oos.close();
                        }catch (Exception ex){}
                    }
                    state=RUNNING;//当确认窗口消失后让游戏恢复运行
                }

                if (state==RUNNING) {//仅在运行状态下进行
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {//若按下的是空格
                        Bomb obj = ship.shootBomb();
                        bombs = Arrays.copyOf(bombs, bombs.length + 1);
                        bombs[bombs.length - 1] = obj;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        ship.moveRight();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        ship.moveLeft();
                    }
                }
            }
        };
        this.addKeyListener(k);//添加侦听

        Timer timer=new Timer();
        int interval=10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {//仅在运行时进行
                    submarinesEnterAction();//潜艇(侦察潜艇、鱼雷潜艇、水雷潜艇)入场
                    mineEnterAction(); //水雷入场
                    moveAction();     //海洋对象移动
                    outOfBoundsAction();//删除越界对象
                    bombBangAction(); //炸弹与潜艇碰撞
                    mineBangAction(); //水雷与战舰碰撞
                    checkGameOverAction();
                    repaint();  //重画---系统自动调用
                }
            }
        },interval,interval);
    }

    //重写paint()画笔 g:系统自带的画笔
    public void paint(Graphics g){ //每10毫秒走一次
        Images.sea.paintIcon(null,g,0,0);
        ship.paintImage(g);
        for (int i=0;i<submarines.length;i++){ //遍历数组
            submarines[i].paintImage(g); //画潜艇
        }
        for (int i=0;i<mines.length;i++){
            mines[i].paintImage(g); //画水雷
        }
        for (int i=0;i<bombs.length;i++){
            bombs[i].paintImage(g); //画炸弹
        }
        g.drawString("SCORE:"+score,200,50);
        g.drawString("LIFE:"+ship.getLife(),400,50);

        if (state==GAME_OVER){//若当前为游戏结束状态
            Images.gameover.paintIcon(null,g,0,0);
        }
    }

    public static void main(String[] args) {
        JFrame frame=new JFrame();
        World world=new World();
        world.setFocusable(true);
        frame.add(world);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH+16,HEIGHT+39);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        world.action(); //启动程序的执行
    }
}
