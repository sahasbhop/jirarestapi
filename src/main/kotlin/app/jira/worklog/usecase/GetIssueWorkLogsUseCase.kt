package app.jira.worklog.usecase

import app.ApplicationContext
import app.jira.worklog.model.WorkLog
import app.jira.worklog.repository.WorkLogRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class GetIssueWorkLogsUseCase(private val context: ApplicationContext) {

    fun execute(issueIds: List<String>): Observable<List<WorkLog>> {
        return Observable.create<List<WorkLog>> { observer ->
            val executorService = Executors.newFixedThreadPool(context.maxConcurrentRequest)

            val observables = issueIds.map { issueId ->
                WorkLogRepository(context).getIssueWorkLogs(issueId)
                        .retry(3)
                        .map { it.worklogs }
                        .subscribeOn(Schedulers.from(executorService))
            }

            Observable.zip(observables) {
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
            }.blockingSubscribe({
                executorService.shutdown()
                observer.onNext(it)
                observer.onComplete()
            }, { error ->
                executorService.shutdown()
                observer.onError(error)
            })
        }


    }
}