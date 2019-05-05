package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private int width;
    private int height;
    private Paint paint;
    private Snake snake;
    private Paint foodPaint;
    private Paint textPaint;

    private int Score = 3000;
    private int guanka = 0;

    private  Snake.Point food;

    public GameSurfaceView(Context context) {
        super(context);
        holder = this.getHolder();
        holder.addCallback(this);
    }

    public void initFood() {
        //需要判断 刷出来的食物，不在蛇身上
        int x;
        int y;
        do {
            x = (int) (Math.random() * width);
            y = (int) (Math.random() * height);
        } while (snake.isConnPoint(x, y));
        food = new Snake.Point(x, y);
    }


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void run() {
        //游戏控制
        //只有游戏没有结束 就一直播放

        //逻辑判断
        initFood();
        while (!snake.isOver()) {
            //贴图
            Canvas canvas = holder.lockCanvas();
            myDraw(canvas);
            holder.unlockCanvasAndPost(canvas);
            snake.move();
            if (snake.eat(food)) {
                //吃到了 分数就增加
                Score += (guanka * 2 + 1) * 100;
                if (Score > 10000)
                    guanka = 5;
                else if (Score > 7000)
                    guanka = 4;
                else if (Score > 3000)
                    guanka = 3;
                else if (Score > 1000)
                    guanka = 2;
                else if (Score > 500)
                    guanka = 1;
                else
                    guanka = 0;
                initFood();
            }
            try {
                // 0 --300      5 -- 50
                Thread.sleep(300 - guanka * 50);//控制速度
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.RED);
        paint.setTextSize(72);
        canvas.drawText("GameOver", getWidth() / 2, getHeight() / 2, paint);
        holder.unlockCanvasAndPost(canvas);
    }

    private Thread th;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initView();
        //自己启动自己
        th = new Thread(this);
        th.start();
    }

    private void initView() {
        //算
        width = getWidth() / 40;    //列
        height = getHeight() / 40;  //行
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.argb(0x80, 0xcc, 0xcc, 0xcc));
        snake = new Snake(width, height);
        foodPaint = new Paint(paint);
        foodPaint.setColor(Color.RED);
        textPaint = new Paint(paint);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(30);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            th.interrupt();
            th.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //将我所有需要绘制界面的方法，都丢到这里
    public void myDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        //首先，线条
        for (int i = 0; i < width; i++) {
            canvas.drawLine(i * 40, 0, i * 40, getHeight(), paint);
        }
        for (int i = 0; i < height; i++) {
            canvas.drawLine(0, i * 40, getWidth(), i * 40, paint);
        }
        //绘制蛇
        snake.onDraw(canvas);
        //绘制食物
        canvas.drawRect(food.x * 40, food.y * 40, food.x * 40 + 40, food.y * 40 + 40, foodPaint);

        canvas.drawText("分数:" + Score, 20, 20, textPaint);
        canvas.drawText("关卡:" + guanka, 20, 50, textPaint);
    }


    private float oldx, oldy;

    //手势方向改变
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldx = x;
                oldy = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断
                if (Math.abs(oldx - x) > Math.abs(oldy - y)) {
                    //左右滑动
                    if (oldx - x > 15) { //左滑
                        snake.changeDir(Snake.LEFT);

                    } else if (oldx - x < -15) {//右滑
                        snake.changeDir(Snake.RIGHT);

                    }
                } else {
                    //上下滑动
                    if (oldy - y > 15) { //上滑
                        snake.changeDir(Snake.UP);

                    } else if (oldy - y < -15) {//下滑

                        snake.changeDir(Snake.DOWN);
                    }
                }


                break;
        }
        return true;
    }
}
