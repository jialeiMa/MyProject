package com.mjl.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

/**
 * @Author: mjl
 * @Date: 2020/12/19 12:35
 * @Description: 游戏界面
 */
public class GamePanel extends JPanel implements KeyListener, ActionListener {

    //蛇的长度
    int length;
    //蛇的坐标
    int[] snakeX = new int[600];
    int[] snakeY = new int[500];
    //蛇头方向 R:右 L:左 U:上 D:下
    String director;

    //游戏启动状态
    boolean isStart = false;

    //游戏失败状态
    boolean isDead = false;

    //游戏通关
    boolean isPass = false;

    //定时器
    Timer timer = new Timer(100, this);

    //食物坐标
    int foodx;
    int foody;
    Random random = new Random();

    //积分
    int score;

    //构造器中初始化
    public GamePanel() {
        init();
        //获取键盘焦点，获取监听事件
        this.setFocusable(true);
        this.addKeyListener(this);//参数为监听对象，当前界面
    }

    //初始化
    public void init() {
        //初始化蛇长度
        length = 3;
        //蛇头初始化位置
        snakeX[0] = 100;
        snakeY[0] = 100;
        //第一节身体坐标
        snakeX[1] = 75;
        snakeY[1] = 100;
        //第二节身体坐标
        snakeX[2] = 50;
        snakeY[2] = 100;

        //初始化蛇头方向
        director = "R";

        //初始化食物位置
        int foodxx = 25 + 25 * (random.nextInt(34));
        int foodyy = 75 + 25 * (random.nextInt(24));
        int[] res = foodxy(foodxx, foodyy);
        foodx = res[0];
        foody = res[1];

        //初始化积分 = 0
        score = 0;

        //启动定时器
        timer.start();
    }


    //画板:画界面，画蛇
    //参数Graphics 绘图，画笔
    @Override
    protected void paintComponent(Graphics g) {
        //清屏
        super.paintComponent(g);
        //设置背景颜色
        this.setBackground(Color.BLACK);
        //把图片资源画到画板上  此处报空指针异常，可以查看文件路径是否写错；或者删除掉out文件夹，重新编译
        Data.header.paintIcon(this, g, 25, 11);
        //绘制游戏区域
        g.fillRect(25, 75, 850, 600);

        //绘制一条静态蛇，蛇头需要根据方向来变
        switch (director) {
            case "R":
                Data.right.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "L":
                Data.left.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "U":
                Data.up.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
            case "D":
                Data.down.paintIcon(this, g, snakeX[0], snakeY[0]);
                break;
        }

        for (int i = 1; i < length; i++) {
            Data.body.paintIcon(this, g, snakeX[i], snakeY[i]);
        }

        //画食物
        Data.food.paintIcon(this, g, foodx, foody);

        //积分
        g.setColor(Color.WHITE);
        g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        g.drawString("长度：" + length, 750, 35);
        g.drawString("分数：" + score, 750, 55);

        //游戏提示
        if (isStart == false) {
            //提示文字,String
            g.setColor(Color.WHITE);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("按下空格键开始", 300, 300);
        }

        //失败提醒
        if (isDead == true) {
            g.setColor(Color.RED);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("游戏失败，按下空格键重新开始", 200, 300);
        }

        //通关提醒
        if (isPass == true) {
            g.setColor(Color.RED);
            g.setFont(new Font("微软雅黑", Font.BOLD, 40));
            g.drawString("恭喜通关，按下空格键重新开始", 200, 300);
        }

    }




    //键盘监听事件，按下即可
    @Override
    public void keyPressed(KeyEvent e) {
        //接收键盘输入
        int keycode = e.getKeyCode();
        switch (keycode) {
            case KeyEvent.VK_SPACE:
                if (isDead == true) {
                    isDead = false;
                    init();
                } else if (isPass == true){
                    isPass = false;
                    init();
                } else {
                    isStart = !isStart;
                }
                repaint();//刷新界面
                break;
            case KeyEvent.VK_LEFT:
                director = "L";
                break;
            case KeyEvent.VK_RIGHT:
                director = "R";
                break;
            case KeyEvent.VK_UP:
                director = "U";
                break;
            case KeyEvent.VK_DOWN:
                director = "D";
                break;
        }


    }

    //执行定时操作
    @Override
    public void actionPerformed(ActionEvent e) {
        int velocity = 100 - score;
        if (velocity <= 50) {
            isPass = true;
            repaint();
        }
        timer.setDelay(velocity);
        //如果游戏处于启动状态,并且尚未失败
        if (isStart && isDead == false && isPass == false) {
            //右移，蛇头右移，身体跟着右移。每一个元素，在下一次的位置是前一个元素的位置
            //这块儿为什么是这样写
            for (int i = length - 1; i > 0; i--) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            //这样写，为什么身体少一节
//            for (int i = 1; i < length; i++) {
//                snakeX[i] = snakeX[i-1];
//                snakeY[i] = snakeY[i-1];
//            }

            //通过控制方向让头部移动
            //边界判断，可穿越
//            if (director.equals("R")) {
//                snakeX[0] = snakeX[0] + 25;
//                //边界判断
//                if (snakeX[0] > 850) {
//                    snakeX[0] = 25;
//                }
//            } else if (director.equals("L")) {
//                snakeX[0] = snakeX[0] - 25;
//                //边界判断
//                if (snakeX[0] < 25) {
//                    snakeX[0] = 850;
//                }
//            } else if (director.equals("U")) {
//                snakeY[0] = snakeY[0] - 25;
//                //边界判断
//                if (snakeY[0] < 75) {
//                    snakeY[0] = 650;
//                }
//            } else if (director.equals("D")) {
//                snakeY[0] = snakeY[0] + 25;
//                //边界判断
//                if (snakeY[0] > 650) {
//                    snakeY[0] = 75;
//                }
//            }

            //边界判断不可穿越
            switch (director) {
                case "R":
                    snakeX[0] = snakeX[0] + 25;
                    if (snakeX[0] > 850) {
                        isDead = true;
                    }
                    break;
                case "L":
                    snakeX[0] = snakeX[0] - 25;
                    if (snakeX[0] < 25) {
                        isDead = true;
                    }
                    break;
                case "U":
                    snakeY[0] = snakeY[0] - 25;
                    if (snakeY[0] < 75) {
                        isDead = true;
                    }
                    break;
                case "D":
                    snakeY[0] = snakeY[0] + 25;
                    if (snakeY[0] > 650) {
                        isDead = true;
                    }
                    break;
            }


            //如果蛇头和食物坐标重合，即吃到食物，蛇身体长度+1
            if (snakeX[0] == foodx && snakeY[0] ==foody) {
                length++;
                //左上角新生成的身体闪烁问题，是因为新生成的身体，初始化时snakeX[length] = 0,snakeY[length] = 0;
                //1、让新生成的身体出于界面之外，snakeX[length] = 9999,snakeY[length] = 9999;
                //2、让新生成的身体位于尾巴附近
                snakeX[length-1] = snakeX[length-2];
                snakeY[length-1] = snakeY[length-2];

                score += 10;
                //重新生成食物,注意食物不能和蛇重合
                int foodxxx = 25 + 25 * (random.nextInt(34));
                int foodyyy = 75 + 25 * (random.nextInt(24));
                int[] res = foodxy(foodxxx, foodyyy);
                foodx = res[0];
                foody = res[1];
            }

            for (int i = 1; i < length; i++) {
                if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                    isDead = true;
                }
            }

            repaint();
        }
        //启动定时器
        timer.start();
    }

    //食物坐标初始化，需要注意不能和蛇身体重合
    public int[] foodxy(int foodxx, int foodyy) {
        int[] res = {foodxx, foodyy};
        for (int i = 0; i < length; i++) {
            //如果食物和蛇身体重合，重新生成食物坐标
            if (foodxx == snakeX[i] && foodyy == snakeY[i]) {
                foodxx = 25 + 25 * (random.nextInt(34));
                foodyy = 75 + 25 * (random.nextInt(24));
                foodxy(foodxx, foodyy);
            }
        }
        return res;
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void keyTyped(KeyEvent e) {

    }


}
