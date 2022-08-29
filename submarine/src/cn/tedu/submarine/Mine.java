package cn.tedu.submarine;

import javax.swing.ImageIcon;

//水雷
public class Mine extends SeaObject{


    public Mine(int x,int y) {
        super(11,11,x,y,1);
    }
    public void move(){
        y-=speed; //y- (向上)
    }

    @Override
    public ImageIcon getImage() {
        return Images.mine;
    }


    //检查是否越界
    public boolean isOutBounds(){
        return this.y<=150-this.height;
    }
}
