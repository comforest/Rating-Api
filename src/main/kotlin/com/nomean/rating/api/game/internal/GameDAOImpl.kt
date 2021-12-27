//package com.nomean.rating.api.game.internal
//
//import com.nomean.rating.api.game.GameDao
//import com.nomean.rating.api.game.GameMapping
//import com.nomean.rating.api.game.GameVO
//import org.mybatis.spring.SqlSessionTemplate
//import org.springframework.stereotype.Repository
//import javax.inject.Inject
//
//
//@Repository
//class GameDAOImpl : GameDao {
//    @Inject
//    private lateinit var session: SqlSessionTemplate
//    private val mapper by lazy { session.getMapper(GameMapping::class.java) }
//
//
//    override fun getGameList(): List<GameVO> {
//        return mapper.getGameList()
//
//        //        return session.selectList("getGameList")
//    }
//
//    override fun addGame(gameVO: GameVO) {
//        mapper.addGame(gameVO)
//    }
//}