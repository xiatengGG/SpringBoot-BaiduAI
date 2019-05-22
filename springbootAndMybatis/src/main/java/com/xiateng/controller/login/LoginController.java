package com.xiateng.controller.login;

import com.xiateng.entity.TUser;
import com.xiateng.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class LoginController {
    @Autowired
    private TUserService tUserService;

    /**
     * 初始化登录页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/login_view")
    public String index(Model model){
//        Map<String, Object> map = new HashMap<>();
//        TUser tUser = tUserService.selectByPrimaryKey(1);
//        model.addAttribute("tUsers",tUser);
        return "login";
    }

    /**
     * 登录
     * @param tUser
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String, Object> login(TUser tUser, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        if (tUser == null){
            map.put("result",1);
            map.put("msg","请输入用户名和密码!");
            return map;
        }
        if(tUser.getUserName() == null || "".equals(tUser.getUserName())){
            map.put("result",1);
            map.put("msg","请输入用户名!");
            return map;
        }
        if(tUser.getUserPassword() == null || "".equals(tUser.getUserPassword())){
            map.put("result",1);
            map.put("msg","请输入密码!");
            return map;
        }
        List<TUser> tUsers = tUserService.selectByTUser(tUser);
        if (tUsers==null || tUsers.isEmpty()){
            map.put("result",1);
            map.put("msg","用户名不存在！");
            return map;
        }
        if(!tUser.getUserPassword().equals(tUsers.get(0).getUserPassword())){
            map.put("result",1);
            map.put("msg","密码错误！");
            return map;
        }
        map.put("result",2);
        map.put("msg","登录成功！");
        map.put("userId",tUsers.get(0).getUserId());
        request.getSession().setAttribute("_session_user",tUsers.get(0));

        return map;
    }
}
