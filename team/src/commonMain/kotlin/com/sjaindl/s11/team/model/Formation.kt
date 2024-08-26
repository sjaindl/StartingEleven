package com.sjaindl.s11.team.model

data class Formation(
    val formationId: String,
    val defenders: Int,
    val midfielders: Int,
    val attackers: Int,
) {
    companion object {
        val defaultFormation = Formation(formationId = "4-2-4", defenders = 4, midfielders = 2, attackers = 4)
    }
}
