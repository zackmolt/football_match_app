package com.example.kade.mvp.netserv

object TheSportDBApi {
    fun getNextMatch(league: String?): String {
        return "https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=$league"
    }
    fun getPrevMatch(league: String?): String {
        return "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=$league"
    }
    fun getDetailMatch(match: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=$match"
    }
    fun getDetailLeague(league: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/lookupleague.php?id=$league"
    }
    fun getSearch(query: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/searchevents.php?e=$query"
    }
    fun getSearchTeam(query: String?): String{
        return "https://www.thesportsdb.com/api/v1/json/1/searchteams.php?t=$query"
    }

}