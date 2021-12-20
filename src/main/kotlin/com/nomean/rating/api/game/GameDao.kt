package com.nomean.rating.api.game

import org.mybatis.spring.SqlSessionTemplate
import org.springframework.stereotype.Repository
import javax.inject.Inject


interface GameDao {
    fun getGameList(): List<GameVO>
    fun addGame(gameVO: GameVO)
}