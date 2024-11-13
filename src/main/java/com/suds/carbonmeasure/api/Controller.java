package com.suds.carbonmeasure.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class Controller {

    @RequestMapping(path = {"/billUpload", "/eBillTable", "/register","/logout","/document"})
    public ModelAndView toDashBoardPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return new ModelAndView("forward:/");
    }

    @RequestMapping("/barChart")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return new ModelAndView("forward:/");
    }
}
