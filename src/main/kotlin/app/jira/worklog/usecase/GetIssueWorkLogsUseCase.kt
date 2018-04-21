package app.jira.worklog.usecase

import app.ApplicationContext
import app.jira.worklog.model.WorkLog
import app.jira.worklog.repository.WorkLogRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GetIssueWorkLogsUseCase(private val context: ApplicationContext) {

    fun execute(issueIds: List<String>): Observable<List<WorkLog>> {
        return Observable.create<List<WorkLog>> { observer ->
            val observables = issueIds.map {
                WorkLogRepository(context).getIssueWorkLogs(it)
                        .map { it.worklogs }
                        .subscribeOn(Schedulers.io())
            }

            Observable
                    .zip(observables) {
                        it
                                .filter { obj -> obj is List<*> }
                                .map {
                                    @Suppress("UNCHECKED_CAST")
                                    it as List<WorkLog>
                                }
                                .reduce { acc, list ->
                                    ArrayList<WorkLog>().apply {
                                        addAll(acc)
                                        addAll(list)
                                    }
                                }
                    }.blockingSubscribe {
                        observer.onNext(it)
                        observer.onComplete()
                    }
        }


    }
}