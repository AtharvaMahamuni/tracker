package web.athma.sadhane.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SadhaneDatabaseTest {

    private lateinit var database: SadhaneDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SadhaneDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `todoDao returns a non-null DAO`() {
        val dao = database.todoDao()

        assertNotNull(dao)
    }

    @Test
    fun `getInstance returns the same instance on repeated calls`() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val instance1 = SadhaneDatabase.getInstance(context)
        val instance2 = SadhaneDatabase.getInstance(context)

        assertSame(instance1, instance2)

        // Clean up the singleton for test isolation
        instance1.close()
        val field = SadhaneDatabase::class.java.getDeclaredField("INSTANCE")
        field.isAccessible = true
        field.set(null, null)
    }
}
