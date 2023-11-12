package ntutifm.game.urbanflow.dataBase

import android.content.Context
import androidx.lifecycle.LiveData
import ntutifm.game.urbanflow.apiClass.SearchHistory

class SearchHistoryRepository(context: Context) {
    private val searchHistoryDao : SearchHistoryDao by lazy {
        MapDatabase.getDatabase(context.applicationContext).getSearchHistoryDao()
    }
    val searchHistory:LiveData<List<SearchHistory>> by lazy {
        searchHistoryDao.getAllHistory()
    }
    fun deleteHistory(data:SearchHistory){
        searchHistoryDao.deleteHistory(data.roadName)
    }
    fun insertHistory(data:SearchHistory){
        searchHistoryDao.deleteHistory(data.roadName)
        searchHistoryDao.insertHistory(data)
    }
    fun updateHistory(data:SearchHistory){
        searchHistoryDao.updateHistory(data)
    }
}