package com.itheima.test;

public class test1{
    public static void main(String[] args)
    {
        //定义变量记录秒数
        int seconds = 3661;

        //2.获取小时数
        int hours = seconds / 3600;
        System.out.println(hours);
        //3.获取分钟数
        //总秒数-小时数 3661-1600=61
        //seconds %3600
        int min=seconds%3600/60;
        System.out.println(min);
        //4.获取秒数
        int second=seconds%3600%60;
        System.out.println(second);
    }
}
