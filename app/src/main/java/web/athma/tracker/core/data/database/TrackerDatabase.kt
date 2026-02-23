package web.athma.tracker.core.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import web.athma.tracker.feature.todoApp.data.Todo
import web.athma.tracker.feature.todoApp.data.TodoDao

@Database(entities = [Todo::class], version = 1)
abstract class TrackerDatabase : RoomDatabase(){
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: TrackerDatabase? = null

        fun getInstance(context: Context): TrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrackerDatabase::class.java,
                    "athmaWebTrackerDatabase"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}