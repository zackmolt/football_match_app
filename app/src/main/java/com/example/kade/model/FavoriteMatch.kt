package com.example.kade.model

data class FavoriteMatch(
    val id: Long?,
    val eventId: Int?,
    val strHome: String?,
    val strAway: String?,
    val intHomeScore:Int?,
    val intAwayScore:Int?,
    val strBadgeHome: String?,
    val strBadgeAway: String?,
    val idHome: Int?,
    val idAway: Int?,
    val dateEvent: String?,
    val strTime: String?
) {
    companion object {
        const val TABLE_FAV: String = "TABLE_FAVORITE_MATCH"
        const val TABLE_ALERT: String = "TABLE_ALERT_MATCH"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val STR_HOME: String = "STR_HOME"
        const val STR_AWAY: String = "STR_AWAY"
        const val INT_HOME_SCORE: String = "INT_HOME_SCORE"
        const val INT_AWAY_SCORE: String = "INT_AWAY_SCORE"
        const val STR_BADGE_HOME: String = "STRBADGE_HOME"
        const val STR_BADGE_AWAY: String = "STRBADGE_AWAY"
        const val ID_HOME: String = "ID_HOME"
        const val ID_AWAY: String = "ID_AWAY"
        const val DATE_EVENT: String = "DATE_EVENT"
        const val STR_TIME: String = "STR_TIME"

        //TEAM CONS
        const val TABLE_TEAM: String = "TABLE_TEAM"
        const val STR_TEAM_NAME: String = "TEAM_NAME"
        const val STR_TEAM_URL: String = "TEAM_URL"
        const val STR_TEAM_ID: String = "TEAM_ID"
    }
}