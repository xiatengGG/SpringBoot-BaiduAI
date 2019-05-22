package com.xiateng.controller.test;

import com.xiateng.entity.TUser;
import com.xiateng.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class StarterController {

    @Autowired
    private TUserService tUserService;

    @RequestMapping(value = "/detail")
    public String index(Model model,int userId){
        Map<String, Object> map = new HashMap<>();
        TUser tUser = tUserService.selectByPrimaryKey(userId);
        model.addAttribute("tUsers",tUser);
        return "index";
    }
}
