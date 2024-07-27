package com.tappytaps.storky.repository

import com.tappytaps.storky.data.StorkyDatabaseDao
import com.tappytaps.storky.model.Contraction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ContractionsRepository @Inject constructor(private val storkyDatabaseDao: StorkyDatabaseDao) { //repository for save and load contractions
    suspend fun addContraction(contraction: Contraction) = storkyDatabaseDao.insert(contraction = contraction)
    suspend fun updateContraction(contraction: Contraction) = storkyDatabaseDao.update(contraction)
    suspend fun deleteContraction(contraction: Contraction) = storkyDatabaseDao.deleteContraction(contraction)
    suspend fun deleteContractionById(contractionId: String) = storkyDatabaseDao.deleteContractionById(contractionId)

    suspend fun deleteAllContractions() = storkyDatabaseDao.deleteAll()
    fun getAllActiveContractions(): Flow<List<Contraction>> = storkyDatabaseDao.getActiveContractions().flowOn(Dispatchers.IO).conflate()
    suspend fun getContraction(id: String): Contraction = storkyDatabaseDao.getContractionById(id=id)

    suspend fun deleteAllActiveContractions() = storkyDatabaseDao.deleteAllActiveContractions()

    //history
    fun getAllHistoryContractions(): Flow<List<Contraction>> = storkyDatabaseDao.getHistoryContractions().flowOn(Dispatchers.IO).conflate()
    suspend fun deleteAllHistoryContractions() = storkyDatabaseDao.deleteAllHistory()
    suspend fun deleteHistoryContractionsBySet(set: Int) = storkyDatabaseDao.deleteHistoryBySet(setValue = set)

    suspend fun deleteContractionsBySet(set: Int) = storkyDatabaseDao.deleteContractionsBySet(setValue = set)


   // Update a list of contractions to history*/
    suspend fun updateContractionsToHistory(contractions: List<Contraction>) {
        val set = (storkyDatabaseDao.getLastRowInHistory()?.in_set ?: 0) +1 //if now row, then return is null and is set as 0, otherwise storkyDatabaseDao.getLastRowInHistory()?.set
        val updatedContractions = contractions.map { it.copy(inHistory = true,
            in_set = set) } //it is necesarry to change this attributes
        storkyDatabaseDao.updateContractions(updatedContractions)
    }

}