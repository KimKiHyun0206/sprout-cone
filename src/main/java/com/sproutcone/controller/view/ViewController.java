package com.sproutcone.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    /**
     * 사이트의 메인 페이지
     * */
    @RequestMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 로그인 페이지
     * */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 마이페이지
     *
     * @implNote 자신의 정보 조회 및 자신이 생성한 런닝 아트를 동시에 조회하도록 하는가?
     * */
    @RequestMapping("/me")
    public String me() {
        return "me";
    }

    /**
     * 런닝 아트 생성 페이지
     * */
    @RequestMapping("/running-art-generate")
    public String runningArtGenerate() {
        return "running_art_generate";
    }

    /**
     * 런닝 아트 조회 페이지
     *
     * @param id 조회할 런닝 아트의 ID
     * @param model Model 객체
     * */
    @RequestMapping("/running-art/{id}")
    public String runningArt(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "running_art";
    }

/*    @RequestMapping("/running-art-list")
    public String runningArtList() {
        return "running_art_list";
    }*/
}