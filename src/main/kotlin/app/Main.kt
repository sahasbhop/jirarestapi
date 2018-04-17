package app

import app.retrofit.util.service
import app.worklog.service.WorkLogService

fun main(args: Array<String>) {
    val context = ApplicationContext.default()
    println(context)

    val workLogService = context.service<WorkLogService>()
    val call = workLogService.updatedList().execute()
}

