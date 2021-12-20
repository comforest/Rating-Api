package com.nomean.rating.api.game

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/games")
class GameController(private val service: GameService) {

    @GetMapping("")
    fun getGameList(): List<GameVO> {
        return service.getGameList()
    }
}