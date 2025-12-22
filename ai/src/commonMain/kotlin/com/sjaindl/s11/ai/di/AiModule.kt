package com.sjaindl.s11.ai.di

import com.sjaindl.s11.ai.data.AiRepositoryImpl
import com.sjaindl.s11.ai.data.remote.AiService
import com.sjaindl.s11.ai.data.remote.KtorAiService
import com.sjaindl.s11.ai.domain.AiRepository
import com.sjaindl.s11.ai.domain.usecase.GetAiCompletionUseCase
import com.sjaindl.s11.ai.ui.ChatViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val aiModule = module {
    single<AiService> {
        KtorAiService()
    }

    single<AiRepository> {
        AiRepositoryImpl(get())
    }

    factory {
        GetAiCompletionUseCase(get())
    }

    factory {
        ChatViewModel(get())
    }

    single<GetAiCompletionUseCase> {
        GetAiCompletionUseCase( get() )
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
}
