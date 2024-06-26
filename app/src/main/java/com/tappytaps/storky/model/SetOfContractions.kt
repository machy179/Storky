package com.tappytaps.storky.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

data class SetOfContractions(
    var listOfContractions: Flow<List<Contraction>>

    ) {

    fun getContractionsCount(): Int = runBlocking {
        // Collect the flow and get the first emitted list
        val contractionsList = listOfContractions.first()
        // Return the size of the list
        contractionsList.size
    }

    fun getListOfContractionsFlowAsList(): List<Contraction> = runBlocking {
        // Collect the flow and get the first emitted list
        listOfContractions.first()
    }

    }

