package com.foogaro.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class UserControllerAdvice {

    @ModelAttribute("currentUserName")
    String currentUser(Principal principal) {
        return (principal != null) ? principal.getName() : null;
    }

    @ModelAttribute("httpSession")
    HttpSession httpSession(HttpSession httpSession) {
        return httpSession;
    }

    @ModelAttribute("sessionAttributeNames")
    List<String> getSessionAttributeNames(HttpSession httpSession) {
        return Collections.list(httpSession.getAttributeNames());
    }

}
