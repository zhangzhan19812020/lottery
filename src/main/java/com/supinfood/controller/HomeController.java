package com.supinfood.controller;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.supinfood.model.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Authentication authentication, Model model) {
        model.addAttribute("trueName",((SysUser)authentication.getPrincipal()).getTrueName());
        model.addAttribute("drawTimes",((SysUser)authentication.getPrincipal()).getDrawTimes() + "次");
        return "index";
    }

    @GetMapping("/login")
    public String login(Authentication authentication, Model model) {
        return "login";
    }




}
