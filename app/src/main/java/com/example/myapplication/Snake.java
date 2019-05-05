package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;


public class Snake {

    public static final int LEFT = 11;
    public static final int UP = 22;
    public static final int RIGHT = 13;
    public static final int DOWN = 24;

    private int x, y;
    private Paint paint = new Paint();
    //蛇身
    private List<Point> mList = new ArrayList<>();

    private int direction = RIGHT;
    private int tempdir;

    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
        mList.add(new Point(x / 2, y / 2));
        mList.add(new Point(x / 2 + 1, y / 2));
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }


    public void changeDir(int dir) {
        this.tempdir = dir;
    }

    //改变方向
    private void changeDir() {
        //延迟操作
        if (tempdir / 10 == direction / 10)
            return;
        //上下和左右
        this.direction = tempdir;
    }

    public void onDraw(Canvas canvas) {
        for (Point p : mList) {
            canvas.drawRect(p.x * 40, p.y * 40, p.x * 40 + 40, p.y * 40 + 40, paint);
        }
    }

    public boolean isOver() {
        //蛇死
        Point point = mList.get(0);
        if (point.x < 0 || point.y < 0 || point.x == x || point.y == y) {
            return true;
        }
        //咬到自己了
        for (int i = 1; i < mList.size(); i++) {
            if (point.x == mList.get(i).x && point.y == mList.get(i).y)
                return true;
        }

        return false;
    }

    //移动
    public void move() {
        changeDir();
        Point point;
        if (food != null) {
            point = food;
        } else {
            point = mList.remove(mList.size() - 1);
        }
        switch (direction) {
            case LEFT:
                point.x = mList.get(0).x - 1;
                point.y = mList.get(0).y;
                break;
            case UP:
                point.x = mList.get(0).x;
                point.y = mList.get(0).y - 1;
                break;
            case RIGHT:
                point.x = mList.get(0).x + 1;
                point.y = mList.get(0).y;
                break;
            case DOWN:
                point.x = mList.get(0).x;
                point.y = mList.get(0).y + 1;
                break;
        }
        mList.add(0, point);
        if (food != null)
            food = null;

    }

    static class Point {
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x;
        int y;
    }

    private Point food;

    //吃
    public boolean eat(Point food) {
        if (mList.get(0).x == food.x && mList.get(0).y == food.y) {
            this.food = food;
            return true;
        }
        return false;
    }

    public boolean isConnPoint(int x, int y) {
        for (int i = 0; i < mList.size(); i++) {
            if (x == mList.get(i).x && y == mList.get(i).y)
                return true;
        }
        return false;
    }
}
