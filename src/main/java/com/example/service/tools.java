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

    /*
 * Java文件操作 获取文件扩展名
 *
 *  Created on: 2011-8-2
 *      Author: blueeagle
 */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 从路径获取文件名
     * @param filepath
     * @return
     */
    public static String getFileName(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int dot = filepath.lastIndexOf('/');
            if ((dot >-1) && (dot < (filepath.length() - 1))) {
                return filepath.substring(dot + 1);
            }
        }
        return filepath;
    }

    public static String getExtensionNameForPath(String filepath){
        return getExtensionName(getFileName(filepath));
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     *
     *  Created on: 2011-8-2
     *      Author: blueeagle
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

}



