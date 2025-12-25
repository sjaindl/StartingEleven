package com.sjaindl.s11.assistant.di

import com.sjaindl.s11.assistant.data.AssistantRepositoryImpl
import com.sjaindl.s11.assistant.data.ChatMessageDataSource
import com.sjaindl.s11.assistant.data.remote.AssistantService
import com.sjaindl.s11.assistant.data.remote.KtorAssistantService
import com.sjaindl.s11.assistant.database.ChatDatabase
import com.sjaindl.s11.assistant.database.createDatabase
import com.sjaindl.s11.assistant.domain.AssistantRepository
import com.sjaindl.s11.assistant.domain.usecase.GetAssistantCompletionUseCase
import com.sjaindl.s11.assistant.ui.ChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val assistantModule = module {
    includes(platformModule)

    single<AssistantService> {
        KtorAssistantService()
    }

    single<AssistantRepository> {
        AssistantRepositoryImpl(get())
    }

    factory {
        GetAssistantCompletionUseCase(get())
    }

    factory {
        ChatViewModel(get(), get(), get())
    }

    single<GetAssistantCompletionUseCase> {
        GetAssistantCompletionUseCase( get() )
    }

    factory {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    factory {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single<ChatDatabase> {
        createDatabase(get())
    }

    single {
        ChatMessageDataSource(get(), get())
    }
}
