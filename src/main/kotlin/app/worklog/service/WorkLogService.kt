package app.worklog.service

import app.worklog.model.UpdatedWorkLogList
import retrofit2.Call
import retrofit2.http.GET

interface WorkLogService {
    @GET("rest/api/2/worklog/updated")
    fun updatedList(): Call<UpdatedWorkLogList>
}