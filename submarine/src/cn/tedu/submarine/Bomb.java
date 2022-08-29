package cn.tedu.submarine;

import javax.swing.ImageIcon;

//炸弹
public class Bomb extends SeaObject {


    public Bomb(int x,int y) {
        super(9,12,x,y,3);
    }
    public void move(){
        y+=speed;   //x+(向下)
    }

    @Override
    public ImageIcon getImage() {
        return Images.bomb;
    }

    //检查是否越界
    public boolean isOutBounds(){
        return this.y>=World.HEIGHT;  //炸弹的y>=窗口的高，即为越界
    }
}
