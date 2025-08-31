package com.example.design_pattern.decoration_pattern.spring;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author L494264Tt@outlook.com
 * @date 2025/8/31 16:46
 */

@RestController
@RequestMapping("/api")
public class MyController {

    @PostMapping
    public Map<String,Object> origin(@TimestampRequestBody Map<String,Object> json){
//        json.put("timestamp",System.currentTimeMillis());
        return json;

    }
}
