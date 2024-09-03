package com.thc.fallspradv.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tbgrant")
@Controller
public class TbgrantController {
    @GetMapping("/{page}")
    public String page(@PathVariable String page){
        return "tbgrant/" + page;
    }


    @GetMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "tbgrant/" + page;
    }
}
