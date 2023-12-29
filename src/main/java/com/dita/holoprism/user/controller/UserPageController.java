package com.dita.holoprism.user.controller;

import com.dita.holoprism.security.auth.PrincipalDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Deprecated
public class UserPageController {
    @GetMapping("/test/login")
    @ResponseBody
    public String testLogin(Authentication authentication) {
        System.out.println("/test/login================"); //TODO

        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) principal;
            System.out.println("principalDetails : " + principalDetails);
        }else if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            System.out.println("oAuth2User : " + oAuth2User);
        }
        return "세션 정보 확인하기";
    }
}
