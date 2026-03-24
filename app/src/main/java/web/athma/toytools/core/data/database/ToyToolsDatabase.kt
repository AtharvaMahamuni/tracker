package web.athma.toytools.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import web.athma.toytools.toy.todoApp.data.Todo
import web.athma.toytools.toy.todoApp.data.TodoDao

/**
 * The central Room database for the Toy Tools app.
 *
 * All toy-specific entities and DAOs are registered here. When adding a new toy
 * that needs local persistence, add its entity to the [entities] list and expose its DAO
 * as an abstract function, then increment [version].
 *
 * Use [getInstance] to obtain the singleton database instance — never instantiate this class directly.
 *
 * Database name: `toyToolsDatabase`
 */
@Database(entities = [Todo::class], version = 1)
abstract class ToyToolsDatabase : RoomDatabase() {

    /** Provides access for doing CRUD operations. */
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: ToyToolsDatabase? = null

        /**
         * Returns the singleton [ToyToolsDatabase] instance, creating it if necessary.
         *
         * Thread-safe: uses double-checked locking via [synchronized] to prevent
         * multiple instances being created on concurrent first calls.
         *
         * @param context Used to get the application context for the database file path.
         */
        fun getInstance(context: Context): ToyToolsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToyToolsDatabase::class.java,
                    "toyToolsDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
