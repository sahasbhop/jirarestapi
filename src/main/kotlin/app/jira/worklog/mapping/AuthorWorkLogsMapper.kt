package app.jira.worklog.mapping

import app.jira.worklog.model.WorkLog

object AuthorWorkLogsMapper {
    fun map(workLogs: List<WorkLog>): Map<String, List<WorkLog>> {
        return workLogs.stream().reduce(mutableMapOf(), { map: MutableMap<String, List<WorkLog>>, workLog: WorkLog ->
            val key = workLog.updateAuthor.name
            val list: MutableList<WorkLog> = map.getOrDefault(key, mutableListOf()) as MutableList<WorkLog>
            list.add(workLog)
            map[workLog.updateAuthor.name] = list
            return@reduce map
        }, { _, _ -> mutableMapOf() })
    }
}