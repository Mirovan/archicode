package ru.bigint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.bigint.service.DiagramService;

@Controller
public class MainController {

    final DiagramService diagramService;

    public MainController(DiagramService diagramService) {
        this.diagramService = diagramService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        String sample = "aaa {\n" +
                "    desc: 'aboutA'\n" +
                "}\n" +
                "\n" +
                "bbb {\n" +
                "    type: 'rect',\n" +
                "    ip: '10.1.2.3',\n" +
                "    desc: 'aboutB'\n" +
                "}\n" +
                "\n" +
                "aaa -> bbb";
        model.addAttribute("data", sample);
        return "index";
    }

    @PostMapping("/")
    public String viewHomePage(Model model, @ModelAttribute("data") String data) {
        model.addAttribute("data", data);
        model.addAttribute("svg", diagramService.createDiagram(data));
        return "index";
    }

//    @GetMapping("/list")
//    public ModelAndView mainPage() {
//        String[] list = {"Cat", "Dog", "Mouse", "Fish", "Elephant"};
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("list", list);
//        modelAndView.setViewName("index");
//
//        return modelAndView;
//    }
}
