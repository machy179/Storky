package com.tappytaps.storky.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tappytaps.storky.model.Contraction
import kotlinx.coroutines.flow.Flow

@Dao
interface StorkyDatabaseDao {
    /*  @Query("SELECT * from contractions_tbl ORDER BY id DESC")    //@Query("SELECT * from contractions_tbl ORDER BY id DESC")
      fun getContractions():
              Flow<List<Contraction>>     //musíme tady mít Flow a ne MutableState, protože Flow je asynchronní...na room knihovnu se to doporučuje
  */
    @Query("SELECT * from contractions_tbl WHERE in_history = false ORDER BY id DESC")
    fun getActiveContractions(): Flow<List<Contraction>> //musíme tady mít Flow a ne MutableState, protože Flow je asynchronní...na room knihovnu se to doporučuje

    @Query("SELECT * from contractions_tbl where id =:id")
    suspend fun getContractionById(id: String): Contraction

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contraction: Contraction)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(contraction: Contraction)

    @Query("DELETE from contractions_tbl")
    suspend fun deleteAll()

    @Query("DELETE from contractions_tbl WHERE in_history = false")
    suspend fun deleteAllActiveContractions()

    @Query("DELETE FROM contractions_tbl WHERE id = :id")
    suspend fun deleteContractionById(id: String)

    @Delete
    suspend fun deleteContraction(contraction: Contraction)

    //for history
    @Query("SELECT * from contractions_tbl WHERE in_history = true ORDER BY id DESC")
    fun getHistoryContractions(): Flow<List<Contraction>>

    @Query("SELECT * FROM contractions_tbl WHERE in_history = true AND in_set = :setValue ORDER BY id DESC")
    fun getHistoryContractionsBySetValue(setValue: Int): Flow<List<Contraction>>

    @Query("DELETE from contractions_tbl WHERE in_history = true")
    suspend fun deleteAllHistory()

    @Query("DELETE from contractions_tbl WHERE in_history = true AND in_set = :setValue")
    suspend fun deleteHistoryBySet(setValue: Int)

    //updating list of Contractions
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContractions(contractions: List<Contraction>)

    //find last row of history - is is used before save active list of contractions to history list of contraction to the
    //database and set up the "set" properties - every saving is increasing "set" attribute
    @Query("SELECT * FROM contractions_tbl WHERE in_history = true ORDER BY id DESC LIMIT 1")
    suspend fun getLastRowInHistory(): Contraction?


}