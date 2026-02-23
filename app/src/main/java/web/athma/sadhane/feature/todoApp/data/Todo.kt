package web.athma.sadhane.feature.todoApp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single todo item stored in the local Room database.
 *
 * @property id Auto-generated primary key. Defaults to 0 so Room assigns the ID on insert.
 * @property task The text content of the todo item.
 * @property isDone Whether the task has been marked as completed.
 */
@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val task: String,
    val isDone: Boolean
)
