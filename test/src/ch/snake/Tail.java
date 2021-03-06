package ch.snake;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Tail {

    private int[] xArray = new int[100];
    private int[] yArray = new int[100];
    private String name;
    private int score=0;
    private int length = 6;
    private boolean alive;
    private Dot mDot = new Dot();
    private Color mColor;

    public Tail(String name){
        this.name=name;
    }


    /**
     * Simulates the snakestale going backwards
     * Also Shifts the X Coordinate back the Tail by one
     *
     * @param lastX is the Coordinate of the SnakeHead and is given back the Tail
     */
    private void refreshX(int lastX) {

        int temp1 = lastX, temp2;
        //shifts the coordinates back the minimum snake spine
        for (int x = 1; x < length; x++) {
            temp2 = xArray[x];
            xArray[x] = temp1;
            temp1 = temp2;
        }
    }

    void reset() {

        for (int x = 0; x < length; x++) {
            xArray[x] = 2000;
        }

        for (int y = 0; y < length; y++) {
            yArray[y] = 2000;
        }
        length=6;
    }

    /**
     * Simulates the snakestale going backwards
     * Also Shifts the Y Coordinate back the Tail by one
     *
     * @param lastY is the Coordinate of the SnakeHead and is given back the Tail
     */
    private void refreshY(int lastY) {

        int temp1 = lastY, temp2;
        //shifts the coordinates back the minimum snake spine
        for (int x = 1; x < length; x++) {
            temp2 = yArray[x];
            yArray[x] = temp1;
            temp1 = temp2;
        }

    }

    /**
     * Sets the Tail and head Colour of the Snake
     *
     * @param color
     */
    void setColor(Color color) {
        mColor = color;
    }

    int getLength() {
        return length;
    }

    /**
     * Checks if the Snake Collides with the Dot, if it collides it becomes one bPart bigger
     */
    void dotCheck() {
        if (yArray[0] == mDot.getY() && xArray[0] == mDot.getX()) {
            //adds one tail part
            length++;
            //sets the new tailpart outside off map so it doesnt show up
            xArray[length - 1] = 900;
            yArray[length - 1] = 900;
            mDot.changeToNewPosition();
        }
    }

    int[] getXCor() {
        return xArray;
    }

    int[] getYCor() {
        return yArray;
    }

    void setPos(int x, int y, InetAddress address){
        try {
            if (address.equals(InetAddress.getLocalHost())){
                SnakeHead.yHead=y;
                SnakeHead.xHead=x;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    Color getColor() {
        return mColor;
    }

    /**
     * refreshes both coordinates
     *
     * @param newY will be the new X coordinate of the head
     * @param newX will be the new Y coordinate of the head
     */
    void refresh(int newY, int newX) {

        //the last y cor gets saved and the head gets updated
        int lastY = yArray[0];
        yArray[0] = newY;
        refreshY(lastY);

        //the last x cor gets saved and the head gets updated
        int lastX = xArray[0];
        xArray[0] = newX;
        refreshX(lastX);


    }

    boolean isAlive() {
        return alive;
    }

    void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
