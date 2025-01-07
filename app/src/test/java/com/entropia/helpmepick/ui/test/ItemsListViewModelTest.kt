package com.entropia.helpmepick.ui.test

import com.entropia.helpmepick.data.Item
import com.entropia.helpmepick.fake.FakeRepository
import com.entropia.helpmepick.rules.TestDispatcherRule
import com.entropia.helpmepick.ui.addedititem.ItemsListViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ItemsListViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()


    @Test
    fun itemsListViewModel_getItems_verifyItemList() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        assertEquals(
            viewModel.categoriesItemUiState.value.currentItems,
            repository.fakeData.itemsList
        )
    }

    @Test
    fun itemsListViewModel_addItem_addedItem() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        val item = Item(id = 4, name = "b", "3d", 0, 0, 0, 0)
        viewModel.addItem(item, testDispatcherRule.testDispatcher)
        assert(repository.fakeData.itemsList.contains(item))
    }

    @Test
    fun itemsListViewModel_updateItem_updatedItem() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        val item = Item(id = 4, name = "b", "3d", 0, 0, 0, 0)
        viewModel.addItem(item, testDispatcherRule.testDispatcher)
        val updatedItem = Item(id = 4, name = "b", "3d", 2, 0, 0, 0)
        viewModel.updateItem(updatedItem, testDispatcherRule.testDispatcher)
        assertEquals(repository.fakeData.itemsList.last(), updatedItem)
    }

    @Test
    fun itemsListViewModel_deleteItem_deletedItem() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        viewModel.deleteItem(Item(id = 3, name = "Woman And The Raptor", "3d", 0, 0, 0, 0))
        assert(
            !repository.fakeData.itemsList.contains(
                Item(
                    id = 3,
                    name = "Woman And The Raptor",
                    "3d",
                    0,
                    0,
                    0,
                    0
                )
            )
        )
    }

    @Test
    fun itemsListViewModel_selectItem_selectedItem() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        val item = Item(id = 3, name = "Woman And The Raptor", "3d", 0, 0, 0, 0)
        viewModel.selectItem(item)
        assert(viewModel.itemsListUiState.value.selectedItemsList.contains(item))
    }

    @Test
    fun itemsListViewModel_deselectItem_deselectedItem() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        val item = Item(id = 3, name = "Woman And The Raptor", "3d", 0, 0, 0, 0)
        viewModel.selectItem(item)
        viewModel.deselectItem(item)
        assert(
            !viewModel.itemsListUiState.value.selectedItemsList.contains(
                Item(
                    id = 3,
                    name = "Woman And The Raptor",
                    "3d",
                    0,
                    0,
                    0,
                    0
                )
            )
        )
    }

    @Test
    fun itemsListViewModel_selectAll_selectedAll() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        viewModel.selectAll()
        assertEquals(
            viewModel.itemsListUiState.value.selectedItemsList.toList(),
            repository.fakeData.itemsList
        )
    }

    @Test
    fun itemsListViewModel_clearAll_deselectedAll() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        viewModel.selectAll()
        viewModel.clearAll()
        assertEquals(viewModel.itemsListUiState.value.selectedItemsList.toList(), listOf<Item>())
    }

    @Test
    fun itemsListViewModel_showAll_showedAll() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        viewModel.showAllItems()
        assertEquals(
            viewModel.categoriesItemUiState.value.currentItems,
            repository.fakeData.itemsList
        )
    }

    @Test
    fun itemsListViewModel_showCurrent_showedCategory() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        viewModel.showCurrentCategory("3d")
        assertTrue(viewModel.categoriesItemUiState.value.currentItems.filter { it.category == "3d" } == viewModel.categoriesItemUiState.value.currentItems)
    }

    @Test
    fun itemListViewModel_updateEditedItem_updatedItem() = runTest {
        val repository = FakeRepository()
        val viewModel = ItemsListViewModel(repository)
        viewModel.updateEditedItem("B", "3d")
        assertEquals(viewModel.editedItem, Pair("B", "3d"))
    }

}