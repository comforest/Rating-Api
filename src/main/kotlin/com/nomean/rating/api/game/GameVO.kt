package com.nomean.rating.api.game

data class GameVO(
    val id: Int,
    val name: String,
    val minNumber: Int,
    val maxNumber: Int,
) {
    constructor(
        name: String,
        minNumber: Int,
        maxNumber: Int
    ) : this(-1, name, minNumber, maxNumber)

    private constructor() : this(-1, "", 0, 0)

}

