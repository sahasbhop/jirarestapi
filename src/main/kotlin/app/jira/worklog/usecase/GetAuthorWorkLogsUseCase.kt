package app.jira.worklog.usecase

import app.ApplicationContext
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
                .map {
                    it.stream().reduce(mutableMapOf(), { map: MutableMap<String, List<WorkLog>>, workLog: WorkLog ->
                        val key = workLog.updateAuthor.name
                        val list: MutableList<WorkLog> = map.getOrDefault(key, mutableListOf()) as MutableList<WorkLog>
                        list.add(workLog)
                        map[workLog.updateAuthor.name] = list
                        return@reduce map
                    }, { _, _ -> mutableMapOf() })
                }
    }
}