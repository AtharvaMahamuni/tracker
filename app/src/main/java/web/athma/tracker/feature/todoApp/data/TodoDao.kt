package web.athma.tracker.feature.todoApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todo")
    fun getAllTodos(): Flow<List<Todo>>

    @Update
    suspend fun update(todo: Todo)

    @Query("DELETE FROM todo")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(todo: Todo)
}