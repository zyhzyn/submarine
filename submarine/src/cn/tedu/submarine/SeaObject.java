package cn.tedu.submarine;

import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;
import java.awt.Graphics;

public abstract class SeaObject implements Serializable {
    public static final int LIVE=0;
    public static final int DEAD=1;
    protected int state=LIVE;

    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected int speed;
    //专门诶侦查艇、鱼雷艇、水雷艇提供的
    //因为三种潜艇的width、height的值不一样，所以不能写死
    public SeaObject(int width,int height){
        this.width = width;
        this.height = height;
        x = -width;
        Random rand=new Random();
        y =rand.nextInt(World.HEIGHT-height-150+1)+150 ;
        speed = rand.nextInt(3)+1;
    }
    //战舰、水雷、炸弹提供
    //因为三种对象的width、height、x、y、speed都不一样，所以不能写死
    public SeaObject(int width,int height,int x,int y,int speed){
        this.width = width;
        this.height = height;
        this.x = x;
        this. y = y;
        this.speed = speed;
    }


    public abstract void move();
    //获取对象图片
    public abstract ImageIcon getImage();
    //判断对象是否活着
    public boolean isLive(){
        return state==LIVE;//若活着状态为LIVE，表示活着 返回true 否则返回false
    }
    //判断对象是否死了
    public boolean isDead(){
        return state==DEAD;//若死了状态为DEAD，表示死了 返回true 否则返回false
    }

    //画笔
    public void paintImage(Graphics g){
        if (this.isLive()){
            this.getImage().paintIcon(null,g,x,y);
        }
    }
    //检查是否越界
    public boolean isOutBounds(){
        return this.x>=World.WIDTH;
    }

    //检查碰撞  this:一个对象  other:另一个对象
    public boolean isHit(SeaObject other){
        //假设：this:表示潜艇， other表示炸弹
        int x1=this.x-other.width;//x1: 潜艇的x-炸弹的宽
        int x2=this.x+this.width;//x2：潜艇的x+潜艇的宽
        int y1=this.y-other.height;//y1：潜艇的y-炸弹的高
        int y2=this.y+this.height;//y2：潜艇的y+潜艇的高
        int x=other.x;//x：炸弹的x
        int y=other.y;//y：炸弹的y

        return x>=x1 && x<=x2 && y>=y1 && y<=y2;//x在x1与x2之间，并且，y在y1与y2之间，即为撞上了
    }
    //海洋对象去死
    public void goDead(){
        state=DEAD;
    }
}
