package com.thc.fallspradv.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test")
@Controller
public class TestController {
    @GetMapping("/{page}")
    public String page(@PathVariable String page){
        return "test/" + page;
    }
    @GetMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "test/" + page;
    }
}
