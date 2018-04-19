package app.jira.search.model

import java.time.LocalDate

sealed class SearchPeriod

data class SearchFrom(val from: LocalDate) : SearchPeriod()
data class SearchBetween(val from: LocalDate, val to: LocalDate) : SearchPeriod()