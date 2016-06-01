package com.example.contrial;

import com.example.model.Tmodel;
import com.example.service.TService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */

@RestController
public class ShowContl {

    public static final Logger logger = LoggerFactory.getLogger(ShowContl.class);

    @Autowired
    public TService tService;


    @RequestMapping("/list")
    @ResponseBody
    public String getList(@RequestParam(value = "page" , defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "100")int size,
                          HttpServletRequest request){
        List<Tmodel> tt=tService.getListByPage(page, size);
        logger.info("into.....");
        for(Tmodel t: tt){
            long id = tService.getTid(t.getT7());
            tService.hp(t.getT5(),t.getT2(),id);
        }

        return "success";
    }


    @RequestMapping("/dimg")
    @ResponseBody
    public String dimg(@RequestParam(value = "page" , defaultValue = "1") int page,
                          @RequestParam(value = "size", defaultValue = "0")int size,
                          HttpServletRequest request){
        tService.downloadImages(page, size);
        return "success";
    }



}
