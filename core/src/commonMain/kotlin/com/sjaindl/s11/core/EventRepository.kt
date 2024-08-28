package com.sjaindl.s11.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class Event {
    TeamChanged,
    SaveTeam,
}

interface EventRepository {
    val onNewEvent: Flow<Event>

    suspend fun teamChanged()
    suspend fun saveTeam()
}

class EventRepositoryImpl: EventRepository {

    private val _onNewEvent = MutableSharedFlow<Event>()
    override val onNewEvent = _onNewEvent.asSharedFlow()

    override suspend fun teamChanged() {
        _onNewEvent.emit(value = Event.TeamChanged)
    }

    override suspend fun saveTeam() {
        _onNewEvent.emit(value = Event.SaveTeam)
    }
}
