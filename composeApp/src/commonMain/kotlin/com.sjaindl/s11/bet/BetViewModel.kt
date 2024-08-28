package com.sjaindl.s11.bet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sjaindl.s11.core.firestore.bets.BetsRepository
import com.sjaindl.s11.core.firestore.bets.model.Bet
import com.sjaindl.s11.core.firestore.config.ConfigRepository
import com.sjaindl.s11.core.firestore.user.UserRepository
import com.sjaindl.s11.core.firestore.usermatchday.UserMatchDayRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class BetState {
    data object Initial: BetState()
    data object Loading: BetState()
    data object NoBets: BetState()

    data class Content(
        val bet: Bet,
        val enabled: Boolean,
    ): BetState()
    data class Error(val message: String): BetState()
}

data class UserBet(
    val homeBet: Int,
    val awayBet: Int,
)

class BetViewModel : ViewModel(), KoinComponent {

    private val tag = "BetViewModel"

    private val configRepository: ConfigRepository by inject()
    private val userRepository: UserRepository by inject()
    private val betsRepository: BetsRepository by inject()
    private val userMatchDayRepository: UserMatchDayRepository by inject()

    private var _betState: MutableStateFlow<BetState> = MutableStateFlow(value = BetState.Initial)
    var betState = _betState.asStateFlow()

    private var _userBet: MutableStateFlow<UserBet> = MutableStateFlow(
        value = UserBet(homeBet = 0, awayBet = 0)
    )
    var userBet = _userBet.asStateFlow()

    private var _savedBet = MutableStateFlow(
        value = false,
    )
    var savedBet = _savedBet.asStateFlow()

    init {
        loadBets()
    }

    fun loadBets() = viewModelScope.launch {
        _betState.value = BetState.Loading

        try {
            val currentUser = userRepository.getCurrentUser()
            if (currentUser == null) {
                _betState.value = BetState.Error(message = "Please sign in")
                return@launch
            }

            val currentBet = betsRepository.getBets().lastOrNull()
            if (currentBet == null) {
                _betState.value = BetState.NoBets
                return@launch
            }

            val enabled = configRepository.getConfig()?.bets ?: false

            val userMatchDay = userMatchDayRepository.getUserMatchDays(uid = currentUser.uid).find {
                it.matchDay == currentBet.id
            }

            _userBet.value = UserBet(
                homeBet = userMatchDay?.homeScore ?: 0,
                awayBet = userMatchDay?.awayScore ?: 0,
            )

            _betState.value = BetState.Content(
                bet = currentBet,
                enabled = enabled,
            )
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _betState.value = BetState.Error(message = message)
        }
    }

    fun setHomeBet(score: Int) {
        _userBet.value = userBet.value.copy(
            homeBet = score,
        )
    }

    fun setAwayBet(score: Int) {
        _userBet.value = userBet.value.copy(
            awayBet = score,
        )
    }

    fun submitBet() = viewModelScope.launch {
        val state = betState.value as? BetState.Content ?: return@launch

        _betState.value = BetState.Loading

        val currentUser = userRepository.getCurrentUser()
        if (currentUser == null) {
            _betState.value = BetState.Error(message = "Please sign in")
            return@launch
        }

        try {
            userMatchDayRepository.submitBet(
                uid = currentUser.uid,
                matchDay = state.bet.id,
                homeScore = userBet.value.homeBet,
                awayScore = userBet.value.awayBet,
            )

            _betState.value = state
            _savedBet.value = true
        } catch (exception: Exception) {
            val message = exception.message ?: exception.toString()
            Napier.e(message = message, throwable = exception, tag = tag)
            _betState.value = BetState.Error(message = message)
        }
    }

    fun resetSavedBetState() {
        _savedBet.value = false
    }
}
