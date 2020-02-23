package com.example.memorizewords;

import java.util.HashSet;
import java.util.Set;

public class Test {


    public static void main(String[] args) {
//        Set<String> set=new HashSet();
//        set.add("1");
//        set.add("2");
//        set.add("3");
//
//        for (String s:set){
//            String key=s;
//            System.out.println(key+","+key);
//        }
        String str = "length";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            sb.append("%");
        }
        System.out.println("%" + sb);
    }

}
