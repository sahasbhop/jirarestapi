package app.jira.util

import java.util.*

data class Ids(private val ids: IntArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ids

        if (!Arrays.equals(ids, other.ids)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(ids)
    }
}