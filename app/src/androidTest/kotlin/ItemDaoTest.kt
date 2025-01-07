import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.data.ItemDao
import com.entropia.helpmepick.data.ItemsDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ItemDaoTest {
    private lateinit var itemDao: ItemDao
    private lateinit var itemsDatabase: ItemsDatabase

    private var item1 = Item(1, "Catfood Calculator", "Android", 2, 3, 1)
    private var item2 = Item(2, "Liminal Sauna", "3d", 0, 2, 2)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        itemsDatabase = Room.inMemoryDatabaseBuilder(context = context, ItemsDatabase::class.java)
            .allowMainThreadQueries().build()
        itemDao = itemsDatabase.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        itemsDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDb() = runBlocking {
        addOneItemToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDb() = runBlocking {
        addTwoItemsToDb()
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdate_updateItemInDb() = runBlocking {
        addOneItemToDb()
        val updatedItem = item1.copy(timesSelected = 1)
        updateItem(updatedItem)
        val allItems = itemDao.getAllItems().first()
        assertEquals(allItems[0].timesSelected, 1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDelete_deleteItem() = runBlocking {
        addOneItemToDb()
        itemDao.delete(item1)
        val allItems = itemDao.getAllItems().first()
        Assert.assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItemsByTimesSelected_sortsBySelected() = runBlocking {
        addTwoItemsToDb()
        val mostSelected = itemDao.getAllItemsByTimesSelected().first()
        Assert.assertTrue(mostSelected[0].timesSelected > mostSelected[1].timesSelected)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItemsByTimesPicked_sortsByPicked() = runBlocking {
        addTwoItemsToDb()
        val mostPicked = itemDao.getAllItems().first()
        Assert.assertTrue(mostPicked[0].regularWins > mostPicked[1].regularWins)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItemsByTimesRejected_sortsByRejected() = runBlocking {
        addTwoItemsToDb()
        val mostRejected = itemDao.getAllItemsByTimesRejected().first()
        Assert.assertTrue(mostRejected[0].timesRejected > mostRejected[1].timesRejected)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetNeverSelected_neverSelected() = runBlocking {
        addTwoItemsToDb()
        val neverSelected = itemDao.getNeverSelected().first()
        assertEquals(neverSelected[0].timesSelected, 0)
    }


    private suspend fun addOneItemToDb() {
        itemDao.insert(item1)
    }

    private suspend fun updateItem(item: Item) {
        itemDao.update(item)
    }

    private suspend fun addTwoItemsToDb() {
        itemDao.insert(item1)
        itemDao.insert(item2)
    }
}