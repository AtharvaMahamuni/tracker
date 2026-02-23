package web.athma.sadhane.feature.todoApp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the [Todo] entity.
 *
 * Provides the SQL operations needed to interact with the `todo` table in the Room database.
 * All write operations are `suspend` functions and must be called from a coroutine.
 * [getAllTodos] returns a [Flow] so the UI reacts automatically to database changes.
 */
@Dao
interface TodoDao {

    /**
     * Inserts a new [Todo] into the database.
     *
     * @param todo The todo item to insert. The [Todo.id] is ignored; Room auto-generates it.
     */
    @Insert
    suspend fun insert(todo: Todo)

    /**
     * Returns a [Flow] that emits the full list of todos whenever the table changes.
     */
    @Query("SELECT * FROM todo")
    fun getAllTodos(): Flow<List<Todo>>

    /**
     * Updates an existing [Todo] record matched by its [Todo.id].
     *
     * @param todo The todo item with updated fields.
     */
    @Update
    suspend fun update(todo: Todo)

    /**
     * Deletes all todos from the table permanently.
     */
    @Query("DELETE FROM todo")
    suspend fun deleteAll()

    /**
     * Deletes a specific [Todo] matched by its [Todo.id].
     *
     * @param todo The todo item to delete.
     */
    @Delete
    suspend fun delete(todo: Todo)
}
