package netty.Hello;

import java.math.BigInteger;
import java.util.*;

/**
 * @author Msq
 * @date 2021/3/13 - 16:23
 */
public class test1{

    public static void main(String[] args) {
        process1();

    }

    public static void process1(){

        Scanner sc = new Scanner(System.in);
        String s = sc.next();

        char[] chs = s.toCharArray();

        if(chs.length == 0){
            System.out.println(0);
            return;
        }

        long cur = 0;
        PriorityQueue<BigInteger> pq = new PriorityQueue<>();

        for(int i = 0; i < chs.length; i++){
            if(Character.isDigit(chs[i])){
                cur = chs[i]-'0';
                while(i+1 < chs.length &&  Character.isDigit(chs[i+1]))
                    cur = cur * 10 + (chs[++i]-'0');
                pq.add(BigInteger.valueOf(cur));
            }
        }

//        list.sort((a,b)-> (int) (a-b));
        while(pq.size() > 0)
            System.out.println(pq.poll());

    }



    public static void process(){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int K = sc.nextInt();
        int[] nums = new int[n];

        for(int i = 0; i < n; i++){
            int tmp = sc.nextInt();
            nums[i] = tmp;
        }
        int left = 0, right = K-1;

        HashMap<Integer, Integer> map = new HashMap<>();

        List<Integer> ans = new ArrayList<>();

        while(right < n){

            // 检查窗口内最大值
            for(int i = left; i <= right; i++){
                if(map.containsKey(nums[i]))
                    map.replace(nums[i], map.get(nums[i])+1);
                else
                    map.put(nums[i],1);
            }

            int key = nums[left];
            for(Map.Entry<Integer, Integer> entry: map.entrySet()){
                if(entry.getValue() > map.get(key))
                    key = entry.getValue();
                else if(entry.getValue() == map.get(key) && entry.getKey() < key)
                    key = entry.getKey();
            }
            ans.add(key);

            left++;
            right++;

        }
    }

}