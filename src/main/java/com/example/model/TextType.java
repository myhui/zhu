package com.example.model;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/25.
 */
public enum TextType {

    TITLE(1,"",null),
    SJF(2,"",null),
    WEIZHI(3,"",null),
    FENLIE(4,"",null),
    NEIRONG(5,"",null),
    TUPIAN(6,"",null),
    SHEYINSHI(7,"",null),
    BIAOQIAN(8,"",null),
    MIAOSHU(9,"",null),
    MTU(10,"",null),
    FTU(11,"",null);


    TextType(int type, String context, Map<String,Object> m){
        this.type = type;
        this.m = m;
        this.context = context;
    }
    private int type;
    private String context;
    private Map<String,Object> m;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Map<String, Object> getM() {
        return m;
    }

    public void setM(Map<String, Object> m) {
        this.m = m;
    }


}
