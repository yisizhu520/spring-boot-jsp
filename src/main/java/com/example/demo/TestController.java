package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    @ResponseBody
    public String hello() {
//        System.out.println(user.getLastName());
        return "hello world";
    }


    @RequestMapping(method = RequestMethod.GET, value = "/demo")
    public String Model(Model model) {
        ModelAndView view = new ModelAndView();
        User user = new User();
        user.setLastName("admin");
        model.addAttribute("user", user);
        return "Model";
    }

}