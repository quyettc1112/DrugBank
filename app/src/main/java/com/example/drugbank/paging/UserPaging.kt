package com.example.drugbank.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.drugbank.repository.UserAPIRepository
import com.example.drugbank.respone.UserListResponse
import retrofit2.HttpException
import java.io.IOException

class UserPaging(private val userAPIRepository: UserAPIRepository)
    : PagingSource<Int, UserListResponse.User>() {
    override fun getRefreshKey(state: PagingState<Int, UserListResponse.User>): Int? {
        TODO()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserListResponse.User> {
       TODO()
    }


}
