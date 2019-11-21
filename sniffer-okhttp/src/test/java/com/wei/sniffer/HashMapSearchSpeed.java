package com.wei.sniffer;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 测试HashMap查找效率
 * 性能：全部放在一个集合 > Group分组
 * 性能差异在1000纳秒左右
 */
public class HashMapSearchSpeed {
    @Test
    public void testMapSearch() {
        HashMap<String, Map<String, String>> group = new HashMap<>();
        HashMap<String, String> allItemMap = new HashMap<>();

        for (int i = 0; i < 101; i++) {
            HashMap<String, String> temp = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                temp.put(i + j + "", i + j + "");
            }
            allItemMap.putAll(temp);
            group.put(i + "", temp);
        }

        long totalTime = 0;
        int count = 100;
        for (int i = 0; i < count; i++) {
            long nanoTime = System.nanoTime();
            allItemMap.get(i * i + i + "");
            long l = System.nanoTime() - nanoTime;
            totalTime = totalTime + l;
        }
        System.out.println(totalTime + " " + totalTime / count);

        long totalTimeGroup = 0;
        for (int i = 0; i < count; i++) {
            long nanoTime = System.nanoTime();
            group.get(i * i + "").get(i * i + i + "");
            long l = System.nanoTime() - nanoTime;
            totalTimeGroup = totalTimeGroup + l;
        }
        System.out.println(totalTimeGroup + " " + totalTimeGroup / count);
    }
}