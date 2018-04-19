package app.jira.worklog.usecase

import app.ApplicationContext
import app.jira.worklog.model.WorkLog
import app.jira.worklog.repository.WorkLogRepository
import io.reactivex.Observable
import java.util.*

class GetIssueWorkLogsUseCase(private val context: ApplicationContext) {

    fun execute(issueId: String): Observable<List<WorkLog>> {
        return WorkLogRepository(context).getIssueWorkLogs(issueId).map { it.worklogs }
    }

    fun execute(issueIds: List<String>): Observable<List<WorkLog>> {
        return Observable.fromIterable(issueIds)
                .flatMap { issueId ->
                    WorkLogRepository(context).getIssueWorkLogs(issueId).map { it.worklogs }
                }
                .reduce { t1: List<WorkLog>, t2: List<WorkLog> ->
                    ArrayList<WorkLog>().apply {
                        addAll(t1)
                        addAll(t2)
                    }
                }.toObservable()

    }
}