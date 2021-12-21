package com.nomean.rating.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@Controller
@RequestMapping("/")
class TestController {
    @ResponseBody
    @GetMapping("")
    fun home(): String {
        return "Hello World"
    }


}