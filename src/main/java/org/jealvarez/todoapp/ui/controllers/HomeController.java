package org.jealvarez.todoapp.ui.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
