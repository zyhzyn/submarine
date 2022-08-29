package cn.tedu.submarine;

import javax.swing.ImageIcon;
import java.awt.*;

//战舰
public class Battleship extends SeaObject {

    private int life;

    public Battleship() {
        super(66,26,270,124,20);
        life = 5;
    }

    public void move(){

    }

    @Override
    public ImageIcon getImage() {
        return Images.battleship;
    }


    //发射炸弹---生成炸弹对象
    public Bomb shootBomb(){
        return new Bomb(this.x,this.y);
    }

    //左移
    public void moveLeft(){
        x-=speed;//向左
    }

    //右移
    public void moveRight(){
        x+=speed;//右移
    }

    //战舰增命
    public void addLife(int num){
        life+=num; //命数增num
    }

    //获取命数
    public int getLife(){
        return life;
    }

    //战舰减命
    public void subtractLife(){
        life--;
    }
}
