package com.nomean.rating.api.game

import org.springframework.stereotype.Service

interface GameService {
    fun getGameList() : List<GameVO>
}