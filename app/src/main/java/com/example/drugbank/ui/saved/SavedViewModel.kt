package com.example.drugbank.ui.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.drugbank.paging.UserPaging
import com.example.drugbank.repository.UserAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class SavedViewModel @Inject constructor(
    private val userAPIRepository: UserAPIRepository,
        ) : ViewModel() {

    val moviesList = Pager(PagingConfig(1)) {
        UserPaging(userAPIRepository)
    }.flow.cachedIn(viewModelScope)


    //Api
 //   val detailsMovie = MutableLiveData<UserListResponse>()
//    fun loadDetailsMovie(id: Int) = viewModelScope.launch {
//        loading.postValue(true)
//        val response = repository.getMovieDetails(id)
//        if (response.isSuccessful) {
//            detailsMovie.postValue(response.body())
//        }
//        loading.postValue(false)
//    }
}