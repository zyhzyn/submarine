package cn.tedu.submarine;
import java.util.Random;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import javax.swing.ImageIcon;

//侦查潜艇
public class ObserveSubmarine extends SeaObject implements EnemyScore{


    public ObserveSubmarine() {
        super(63,19);
    }
    public void move(){
        x+=speed;   //x+(向右)
    }

    @Override
    public ImageIcon getImage() {
        return Images.obsersubm;
    }

    public int getScore(){
        return 10;
    }

}
