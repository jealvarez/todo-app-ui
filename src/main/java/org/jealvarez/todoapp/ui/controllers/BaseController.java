package org.jealvarez.todoapp.ui.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class BaseController {

    @ModelAttribute("authenticated")
    public boolean isAuthenticated() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }

}
