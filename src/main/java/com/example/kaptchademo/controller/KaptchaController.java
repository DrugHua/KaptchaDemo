package com.example.kaptchademo.controller;

import com.example.kaptchademo.tools.KaptchaTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 花建文
 * @date 2024/6/12 9:48
 */
@RestController
@RequestMapping("/get")
public class KaptchaController {

    @Autowired
    private KaptchaTools kaptchaTools;

    @RequestMapping("/getCode")
    public Map<String, String> getCode(){
        Map<String, String> res = kaptchaTools.codeByBase64();
        return res;
    }


}
