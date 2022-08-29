package cn.tedu.submarine;

import java.io.Serializable;

/**
 * 保存所有游戏信息的类
 * 使用这个类的实例作为存档对象
 */
public class GameInfo implements Serializable {
    private Battleship ship;//战舰对象
    private SeaObject[] submarines; //潜艇的数组
    private Mine[] mines; //水雷数组
    private Bomb[] bombs; //炸弹数组
    int subEnterIndex;//潜艇入场计数
    private int mineEnterIndex ;//水雷入场计数
    private int score;//玩家得分

    public GameInfo(Battleship ship, SeaObject[] submarines, Mine[] mines, Bomb[] bombs, int subEnterIndex, int mineEnterIndex, int score) {
        this.ship = ship;
        this.submarines = submarines;
        this.mines = mines;
        this.bombs = bombs;
        this.subEnterIndex = subEnterIndex;
        this.mineEnterIndex = mineEnterIndex;
        this.score = score;
    }

    public Battleship getShip() {
        return ship;
    }

    public void setShip(Battleship ship) {
        this.ship = ship;
    }

    public SeaObject[] getSubmarines() {
        return submarines;
    }

    public void setSubmarines(SeaObject[] submarines) {
        this.submarines = submarines;
    }

    public Mine[] getMines() {
        return mines;
    }

    public void setMines(Mine[] mines) {
        this.mines = mines;
    }

    public Bomb[] getBombs() {
        return bombs;
    }

    public void setBombs(Bomb[] bombs) {
        this.bombs = bombs;
    }

    public int getSubEnterIndex() {
        return subEnterIndex;
    }

    public void setSubEnterIndex(int subEnterIndex) {
        this.subEnterIndex = subEnterIndex;
    }

    public int getMineEnterIndex() {
        return mineEnterIndex;
    }

    public void setMineEnterIndex(int mineEnterIndex) {
        this.mineEnterIndex = mineEnterIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
