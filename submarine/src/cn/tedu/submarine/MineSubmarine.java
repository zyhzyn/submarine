package cn.tedu.submarine;

import javax.swing.ImageIcon;
import java.util.Random;

//水雷潜艇
public class MineSubmarine extends SeaObject implements EnemyLife{


    public MineSubmarine() {
        super(63,19);
    }
    public void move(){
        x+=speed;   //x+(向右)
    }

    @Override
    public ImageIcon getImage() {
        return Images.minesubm;
    }

    //发射水雷----生成水雷
    public Mine shootMine(){
        //水雷的x=水雷潜艇的x+水雷潜艇的width
        //水雷的y=水雷潜艇的y-水雷的height
        return new Mine(x+width,y-11);
    }

    public int getLife(){
        return 1;//打掉水雷潜艇 战舰得一条命
    }
}
