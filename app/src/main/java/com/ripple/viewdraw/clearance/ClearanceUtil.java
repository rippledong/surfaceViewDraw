package com.ripple.viewdraw.clearance;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author : dongbowen
 * @time : 2019/05/08
 * @desc :
 * @version: 1.0
 */
public class ClearanceUtil {
    int x, y;
    int[][] mList;
    int mCount;

    public ClearanceUtil(int x, int y, double percent) {
        mList = new int[this.x = x][this.y = y];
        if (percent > 1) {
            percent -= (int) percent;
        }
        mCount = (int) (x * y * percent);
        if (mCount == 0) {
            mCount = x;
        }
        refresh();
    }

    //重新生成整个队列
    public void refresh() {
        int count = mCount;
        Map<Integer, String> map = new HashMap<>();
        while (map.size() != count) {
            map.put(new Random().nextInt(x * y), "0");
        }
        Set<Integer> list = map.keySet();
        for (Integer i : list) {
            mList[i / y][i % y] = 9;
        }
        for (int i = 0, j; i < x; i++) {
            for (j = 0; j < y; j++) {
                mList[i][j] = check(i, j);
            }
        }
    }

    //得到空的数组
    public int[][] getVoidList() {
        int[][] list = new int[x][y];
        for (int i = 0, j; i < x; i++) {
            for (j = 0; j < y; j++) {
//                if (mList[i][j] == 9) {
                list[i][j] = 12;
//                }
            }
        }
        return list;
    }

    public int get(int x, int y) {
        return mList[x][y];
    }

    //判断 周围九宫格有多少雷的方法
    private int check(int x, int y) {
        if (mList[x][y] == 9) {
            return 9;
        }
        int c = 0;
        for (int i = x - 1, j; i <= x + 1; i++) {
            if (i < 0 || i == this.x) {
                continue;
            }
            for (j = y - 1; j <= y + 1; j++) {
                if (j < 0 || j == this.y) {
                    continue;
                }
                if (mList[i][j] == 9) {
                    c++;
                }
            }
        }
        return c;
    }
}
