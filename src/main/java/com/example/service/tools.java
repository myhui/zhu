package com.example.service;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2016/5/25.
 */
public class tools {

    public static void main(String[] args) {


        String tt = "墨西哥白色的酒店第22张图片";
        System.out.print(vTuNum(tt));

    }

    public static Map<String,Object> vTuNum(String t){
        //墨西哥白色的酒店外部实景图-墨西哥白色的酒店第2张图片
        Map<String , Object> m = new HashMap<String, Object>();

        String [] ts = t.split("-");

        if(ts.length==1&&ts[0].endsWith("张图片")){

            m.put("t","");
            m.put("n",0);

            if(ts[0].endsWith("张图片")) {
                int start = ts[0].lastIndexOf("第");
                String n = ts[0].substring(start+1, ts[0].length()-3);
                if (StringUtils.isNumeric(n)){
                    m.put("n", Integer.parseInt(n));
                }
            }


        }else if(ts.length > 1){
            m.put("t",ts[0]);
            m.put("n",0);
            if(ts[1].endsWith("张图片")) {
                int start = ts[1].lastIndexOf("第");
                String n = ts[1].substring(start+1, ts[1].length()-3);
                if (StringUtils.isNumeric(n)){
                    m.put("n", Integer.parseInt(n));
                }
            }

        }

        return m;
    }
}



