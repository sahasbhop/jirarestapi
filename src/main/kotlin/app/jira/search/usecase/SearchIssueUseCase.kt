package app.jira.search.usecase

import app.ApplicationContext
import app.jira.search.model.SearchIssue
import app.jira.search.repository.SearchRepository
import io.reactivex.Observable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SearchIssueUseCase(private val context: ApplicationContext) {

    fun execute(project: String, updatedFrom: LocalDate? = null, updatedTo: LocalDate? = null, maxResults: Int = 500): Observable<List<SearchIssue>> {
        val jql = jql(project, updatedFrom, updatedTo)

        return SearchRepository(context)
                .search(jql, maxResults)
                .map { it.issues }
    }

    private fun jql(project: String, updatedFrom: LocalDate?, updatedTo: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val builder = StringBuilder("project = $project")
        updatedFrom?.let { builder.append(" and updated >= ${it.format(formatter)}") }
        updatedTo?.let { builder.append(" and updated <= ${it.format(formatter)}") }

        return builder.toString()
    }

}