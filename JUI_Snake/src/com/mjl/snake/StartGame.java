package com.mjl.snake;

import javax.swing.*;

/**
 * @Author: mjl
 * @Date: 2020/12/19 12:29
 * @Description: 游戏启动
 */
public class StartGame {
    public static void main(String[] args) {
        //绘制静态窗口
        JFrame frame = new JFrame("贪吃蛇小游戏");

        //设置界面大小
        frame.setBounds(10,10,900,720);

        //设置窗口大小不可改变，因为目前不支持自适应
        frame.setResizable(false);

        //设置关闭事件，可以关掉游戏窗口
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //把画板添加到窗口
        frame.add(new GamePanel());


        //设置窗口可见性
        frame.setVisible(true);
    }
}
