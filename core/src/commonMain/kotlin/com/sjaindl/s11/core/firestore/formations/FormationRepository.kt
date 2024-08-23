package com.sjaindl.s11.core.firestore.formations

import com.sjaindl.s11.core.firestore.formations.model.Formation
import kotlinx.coroutines.flow.Flow

interface FormationRepository {
    suspend fun getFormations(): List<Formation>
    fun getFormationsFlow(): Flow<List<Formation>>
}

class FormationRepositoryImpl(
    private val formationDataSource: FormationDataSource,
): FormationRepository {
    override suspend fun getFormations() = formationDataSource.getFormations()

    override fun getFormationsFlow() = formationDataSource.getFormationsFlow()
}
