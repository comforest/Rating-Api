package com.nomean.rating.api.game

import org.apache.ibatis.annotations.Mapper

@Mapper
interface GameMapping {
    fun getGameList():List<GameVO>
    fun addGame(gameVO: GameVO)

}