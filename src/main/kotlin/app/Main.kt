package app

import app.jira.issue.model.Issue
import app.jira.search.model.SearchPeriod
import app.jira.search.usecase.SearchIssueUseCase
import app.jira.session.usecase.LoginUseCase
import app.jira.worklog.mapping.WorkLogsMapper
import app.jira.worklog.usecase.GetIssueWorkLogsUseCase
import app.jira.worklog.view.AuthorsWorkLogView

fun main(args: Array<String>) {
    val context = ApplicationContext.read("yml/application.yml")

    val projectName = context.projectName
    val searchPeriod: SearchPeriod = context.searchPeriod()

    println("""
        [main] Project Name: ${context.projectName}
        [main] Search Period: $searchPeriod
        """.trimIndent()
    )

    LoginUseCase(context).execute()
            .flatMap {
                SearchIssueUseCase(context).execute(projectName, searchPeriod)
            }
            .map { issues ->
                issues.associateBy({ issue -> issue.id }, { Issue(it.id, it.fields.summary) })
            }
            .flatMap { issues ->
                GetIssueWorkLogsUseCase(context)
                        .execute(issues.map { (id, _) -> id })
                        .map { workLogList -> WorkLogsMapper.workLogsInPeriod(workLogList, searchPeriod) }
                        .map { workLogList -> WorkLogsMapper.authorsWorkLogsMap(workLogList, issues) }
            }
            .subscribe({ viewModelList ->
                AuthorsWorkLogView.show(context, viewModelList)
            }, { error ->
                println("[main] Error !!")
                error.printStackTrace()
            })
}