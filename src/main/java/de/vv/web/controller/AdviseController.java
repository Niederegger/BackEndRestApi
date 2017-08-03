package de.vv.web.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
  This controller is needed to reroute everything not going to /api/... back to index
  this way Angular will handle everything except API-Calls
 */

@Controller
public class AdviseController implements ErrorController {

    @RequestMapping("/error")
    public String index() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return "index.html";
    }
}