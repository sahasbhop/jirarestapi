package app.jira.search.usecase

import app.ApplicationContext
import app.jira.search.model.SearchBetween
import app.jira.search.model.SearchFrom
import app.jira.search.model.SearchIssue
import app.jira.search.model.SearchPeriod
import app.jira.search.repository.SearchRepository
import io.reactivex.Observable
import java.time.format.DateTimeFormatter

class SearchIssueUseCase(private val context: ApplicationContext) {

    fun execute(project: String, period: SearchPeriod, maxResults: Int = 500): Observable<List<SearchIssue>> {
        val jql = jql(project, period)

        return SearchRepository(context)
                .search(jql, maxResults)
                .map { it.issues }
    }

    private fun jql(project: String, period: SearchPeriod): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val builder = StringBuilder("project = $project")

        when (period) {
            is SearchFrom -> builder.append(" and updated >= ${period.from.format(formatter)}")
            is SearchBetween -> builder.append(" and updated >= ${period.from.format(formatter)} and updated <= ${period.to.format(formatter)}")
        }
        return builder.toString()
    }

}