package ch.snake;

public class Coordinates {
    int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(int x,int y){
        this.y=y;
        this.x=x;
    }
}