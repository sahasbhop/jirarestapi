package app.jira.worklog.usecase

import app.ApplicationContext
import app.jira.worklog.mapping.AuthorWorkLogsMapper
import app.jira.worklog.model.WorkLog
import app.jira.worklog.repository.WorkLogRepository
import io.reactivex.Observable
import java.time.OffsetDateTime

class GetAuthorWorkLogsUseCase(private val context: ApplicationContext) {

    fun execute(sinceDateTime: OffsetDateTime): Observable<Map<String, List<WorkLog>>> {
        val workLogRepository = WorkLogRepository(context)

        return workLogRepository.getIdsOfWorkLogsModified(sinceDateTime)
                .map { it.values.map { it.worklogId.toInt() } }
                .flatMap { workLogRepository.getWorkLogs(it.toIntArray()) }
                .map { AuthorWorkLogsMapper.map(it) }
    }
}