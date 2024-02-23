package com.foogaro.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class IndexController {

    @RequestMapping("/setValue")
    public String setValue(@RequestParam(name = "key", required = false) String key,
                           @RequestParam(name = "value", required = false) String value, HttpServletRequest request, Model model) {
        if (!ObjectUtils.isEmpty(key) && !ObjectUtils.isEmpty(value)) {
            request.getSession().setAttribute(key, value);
        }
        return home(request, model);
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("sessionAttributeNames", Collections.list(request.getSession().getAttributeNames()));
        return "home";
    }

}