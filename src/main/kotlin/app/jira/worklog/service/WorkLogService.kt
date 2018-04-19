package app.jira.worklog.service

import app.jira.util.Ids
import app.jira.worklog.model.IssueWorkLog
import app.jira.worklog.model.UpdatedWorkLogList
import app.jira.worklog.model.WorkLog
import io.reactivex.Observable
import retrofit2.http.*

// Reference: https://developer.atlassian.com/cloud/jira/platform/rest/#api-api-2-worklog-list-post
interface WorkLogService {
    @GET("rest/api/2/worklog/updated")
    fun getIdsOfWorkLogsModified(@Query("since") since: Long): Observable<UpdatedWorkLogList>

    @POST("/rest/api/2/worklog/list")
    fun getWorkLogs(@Body ids: Ids): Observable<List<WorkLog>>

    @GET("rest/api/2/issue/{issueId}/worklog")
    fun getIssueWorkLogs(@Path("issueId") issueId: String): Observable<IssueWorkLog>
}