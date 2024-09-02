package com.tappytaps.android.storky.repository

import com.tappytaps.android.storky.data.StorkyDatabaseDao
import com.tappytaps.android.storky.model.Contraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContractionsRepository @Inject constructor(private val storkyDatabaseDao: StorkyDatabaseDao) { //repository for save and load contractions
    suspend fun addContraction(contraction: Contraction) =
        storkyDatabaseDao.insert(contraction = contraction)

    suspend fun deleteContraction(contraction: Contraction) =
        storkyDatabaseDao.deleteContraction(contraction)

    suspend fun deleteContractionById(contractionId: String) =
        storkyDatabaseDao.deleteContractionById(contractionId)

    fun getAllActiveContractions(): Flow<List<Contraction>> =
        storkyDatabaseDao.getActiveContractions().flowOn(Dispatchers.IO).conflate()

    suspend fun deleteAllActiveContractions() = storkyDatabaseDao.deleteAllActiveContractions()

    suspend fun deleteContractionsBySet(set: Int) =
        storkyDatabaseDao.deleteContractionsBySet(setValue = set)

    //history
    fun getAllHistoryContractions(): Flow<List<Contraction>> =
        storkyDatabaseDao.getHistoryContractions().flowOn(Dispatchers.IO).conflate()

    suspend fun deleteAllHistoryContractions() = storkyDatabaseDao.deleteAllHistory()


    // Update a list of contractions to history*/
    suspend fun updateContractionsToHistory(contractions: List<Contraction>) {
        val set = (storkyDatabaseDao.getLastRowInHistory()?.in_set
            ?: 0) + 1 //if now row, then return is null and is set as 0, otherwise storkyDatabaseDao.getLastRowInHistory()?.set
        val updatedContractions = contractions.map {
            it.copy(
                inHistory = true,
                in_set = set
            )
        } //it is necesarry to change this attributes
        storkyDatabaseDao.updateContractions(updatedContractions)
    }

}