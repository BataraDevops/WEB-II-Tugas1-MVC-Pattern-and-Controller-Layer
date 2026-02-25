package com.example.springmvclab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParamController {

    // @RequestParam: data dari query string
    @GetMapping("/greet")
    public String greetWithParam(
            @RequestParam(defaultValue = "Mahasiswa") String name,
            @RequestParam(defaultValue = "Pagi") String waktu,
            Model model) {
        model.addAttribute("greeting", "Selamat " + waktu + ", " + name + "!");
        return "greet";
    }

    // @PathVariable: data dari URL path
    @GetMapping("/greet/{name}")
    public String greetWithPath(@PathVariable String name, Model model) {
        model.addAttribute("greeting", "Halo, " + name + "!");
        return "greet";
    }

    // Kombinasi keduanya
    @GetMapping("/greet/{name}/detail")
    public String greetCombined(@PathVariable String name,
                                @RequestParam(defaultValue = "ID") String lang,
                                Model model) {
        String greeting = lang.equalsIgnoreCase("EN")
                ? "Hello, " + name + "!"
                : "Halo, " + name + "!";
        model.addAttribute("greeting", greeting);
        return "greet";
    }
}