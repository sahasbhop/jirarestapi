package app.jira.worklog.repository

import app.ApplicationContext
import app.jira.util.Ids
import app.jira.worklog.model.UpdatedWorkLogList
import app.jira.worklog.model.WorkLog
import app.jira.worklog.service.WorkLogService
import io.reactivex.Observable
import java.time.OffsetDateTime

class WorkLogRepository(private val context: ApplicationContext) {

    fun getIdsOfWorkLogs(sinceDateTime: OffsetDateTime): Observable<UpdatedWorkLogList> {
        val unixTime = sinceDateTime.toInstant().toEpochMilli()
        return context.service<WorkLogService>().getIdsOfWorkLogs(unixTime)
    }

    fun getWorkLogs(ids: IntArray): Observable<List<WorkLog>> {
        return context.service<WorkLogService>().getWorkLogs(Ids(ids))
    }
}