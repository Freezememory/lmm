package com.wanglj.lmm.portal.service;

public class DynamicProgrammingBackpack {


    public static void main(String[] args) {
        //物品价值,重量,和背包承重
        int[] v = {0, 8, 10, 6, 3, 7, 2};
        int[] w = {0, 4, 6, 2, 2, 5, 1};
        int c = 12;

        //定义二位数组动态规划背包价值和重量
        int[][] m = new int[v.length][c + 1];
        for (int i = 1; i < v.length; i++) {
            for (int j = 1; j <= c; j++) {
                if (j >= w[i])
                    m[i][j] = m[i - 1][j - w[i]] + v[i] > m[i - 1][j] ? m[i - 1][j - w[i]] + v[i] : m[i - 1][j];
                else
                    m[i][j] = m[i - 1][j];
            }
        }
        int max = 0;
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j <= c; j++) {
                if (m[i][j] > max)
                    max = m[i][j];
            }
        }
        System.out.println(max);
    }
}