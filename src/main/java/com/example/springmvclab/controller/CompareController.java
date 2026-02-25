package com.example.springmvclab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class CompareController {

    // Ini return nama template
    @GetMapping("/test/view")
    public String testView(Model model) {
        model.addAttribute("message", "Ini dari @Controller");
        return "test";  // → templates/test.html
    }

    // Tambahkan @ResponseBody → berperilaku seperti @RestController
    @GetMapping("/test/text")
    @ResponseBody
    public String testText() {
        return "Ini dari @Controller + @ResponseBody → text langsung";
    }
}