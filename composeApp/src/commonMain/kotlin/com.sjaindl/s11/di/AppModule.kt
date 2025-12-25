package com.sjaindl.s11.di

import com.sjaindl.s11.assistant.config.AssistantConfig
import com.sjaindl.s11.assistant.config.Provider
import com.sjaindl.s11.assistant.di.assistantModule
import com.sjaindl.s11.firestore.faq.FaqDataSource
import com.sjaindl.s11.firestore.faq.FaqDataSourceImpl
import com.sjaindl.s11.firestore.faq.FaqRepository
import com.sjaindl.s11.firestore.faq.FaqRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.app
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import org.koin.dsl.module
import startingeleven.composeapp.generated.resources.Res
import startingeleven.composeapp.generated.resources.assistant_title
import startingeleven.composeapp.generated.resources.sample_question_1
import startingeleven.composeapp.generated.resources.sample_question_2
import startingeleven.composeapp.generated.resources.welcome_message

val appModule = module {
    includes(assistantModule)
    single<FaqRepository> { FaqRepositoryImpl(playerDataSource = get()) }
    single<FaqDataSource> { FaqDataSourceImpl(firestore = get()) }

    single {
        Firebase.storage
    }

    single {
        getFirebaseFirestore()
    }

    single {
        AssistantConfig(
            provider = Provider.Flowise(
                baseUrl = "https://www.hvtdpstainz.at/flowise/api/v1/prediction/3d0fc477-d898-4a7d-8474-67348965eb28",
            ),
            sampleQuestions = listOf(Res.string.sample_question_1, Res.string.sample_question_2),
            welcomeMessage = Res.string.welcome_message,
            appBarTitle = Res.string.assistant_title,
        )
    }
}

fun getFirebaseFirestore(): FirebaseFirestore {
    return Firebase.firestore(Firebase.app, "s11-prod")
}
