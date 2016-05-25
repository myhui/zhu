package com.example.contrial;

import com.example.model.Tmodel;
import com.example.service.TService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */

@RestController
public class ShowContl {

    @Autowired
    public TService tService;


    @RequestMapping("/list")
    @ResponseBody
    public String getScoreList(){
        List<Tmodel> tt=tService.getListByPage(2);
        for(Tmodel t: tt){
            long id = tService.getTid(t.getT7());
            tService.hp(t.getT5(),t.getT2(),id);
            return t.getT5();
        }

//        return tt;
        return "";
    }



}
