package com.nomean.rating.api.game.internal

import com.nomean.rating.api.game.GameDao
import com.nomean.rating.api.game.GameService
import com.nomean.rating.api.game.GameVO
import org.springframework.stereotype.Service

@Service
class GameServiceImpl(val gameDAO: GameDao) : GameService {


    override fun getGameList(): List<GameVO> {
        return gameDAO.getGameList()
    }
}