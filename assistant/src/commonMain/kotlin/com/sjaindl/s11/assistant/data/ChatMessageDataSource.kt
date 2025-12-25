package com.sjaindl.s11.assistant.data

import com.sjaindl.s11.assistant.data.remote.model.SourceDocument
import com.sjaindl.s11.assistant.data.remote.model.Tool
import com.sjaindl.s11.assistant.database.ChatDatabase
import com.sjaindl.s11.assistant.ui.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class ChatMessageDataSource(
    private val db: ChatDatabase,
    private val json: Json,
): KoinComponent {

    suspend fun getAll(): List<ChatMessage> = withContext(Dispatchers.Default) {
        db.chatMessageQueries.selectAll().executeAsList().map {
            ChatMessage(
                id = it.id,
                text = it.message,
                isFromUser = it.isFromUser == 1L,
                usedTools = it.usedTools?.let { jsonString ->
                    json.decodeFromString<List<Tool>>(jsonString)
                },
                sourceDocuments = it.sourceDocuments?.let { jsonString ->
                    json.decodeFromString<List<SourceDocument>>(jsonString)
                },
            )
        }
    }

    suspend fun insert(message: ChatMessage): Long = withContext(Dispatchers.Default) {
        db.chatMessageQueries.transactionWithResult {
            db.chatMessageQueries.insert(
                message = message.text,
                isFromUser = if (message.isFromUser) 1L else 0L,
                timestamp = 0,
                usedTools = message.usedTools?.let {
                    json.encodeToString(it)
                },
                sourceDocuments = message.sourceDocuments?.let {
                    json.encodeToString(it)
                },
            )
            db.chatMessageQueries.lastInsertRowId().executeAsOne()
        }
    }

    suspend fun update(id: Long, message: ChatMessage) = withContext(Dispatchers.Default) {
        db.chatMessageQueries.updateMessage(
            id = id,
            message = message.text,
            usedTools = message.usedTools?.let {
                json.encodeToString(it)
            },
            sourceDocuments = message.sourceDocuments?.let {
                json.encodeToString(it)
            },
        )
    }

    suspend fun clear() = withContext(Dispatchers.Default) {
        db.chatMessageQueries.clear()
    }
}
