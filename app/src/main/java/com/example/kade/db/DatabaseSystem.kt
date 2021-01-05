package com.example.kade.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.kade.model.FavoriteMatch
import org.jetbrains.anko.db.*

class DatabaseSystem(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1) {
    companion object {
        private var instance: DatabaseSystem? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseSystem {
            if (instance == null) {
                instance = DatabaseSystem(ctx.applicationContext)
            }
            return instance as DatabaseSystem
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            FavoriteMatch.TABLE_FAV, true,
            FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteMatch.EVENT_ID to INTEGER,
            FavoriteMatch.STR_HOME to TEXT,
            FavoriteMatch.STR_AWAY to TEXT,
            FavoriteMatch.INT_HOME_SCORE to INTEGER,
            FavoriteMatch.INT_AWAY_SCORE to INTEGER,
            FavoriteMatch.STR_BADGE_HOME to TEXT,
            FavoriteMatch.STR_BADGE_AWAY to TEXT,
            FavoriteMatch.ID_HOME to INTEGER,
            FavoriteMatch.ID_AWAY to INTEGER,
            FavoriteMatch.DATE_EVENT to TEXT,
            FavoriteMatch.STR_TIME to TEXT
        )

        db.createTable(
            FavoriteMatch.TABLE_ALERT, true,
            FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteMatch.EVENT_ID to INTEGER,
            FavoriteMatch.STR_HOME to TEXT,
            FavoriteMatch.STR_AWAY to TEXT,
            FavoriteMatch.INT_HOME_SCORE to INTEGER,
            FavoriteMatch.INT_AWAY_SCORE to INTEGER,
            FavoriteMatch.STR_BADGE_HOME to TEXT,
            FavoriteMatch.STR_BADGE_AWAY to TEXT,
            FavoriteMatch.ID_HOME to INTEGER,
            FavoriteMatch.ID_AWAY to INTEGER,
            FavoriteMatch.DATE_EVENT to TEXT,
            FavoriteMatch.STR_TIME to TEXT
        )

        db.createTable(
            FavoriteMatch.TABLE_TEAM, true,
            FavoriteMatch.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteMatch.STR_TEAM_ID to INTEGER,
            FavoriteMatch.STR_TEAM_NAME to TEXT,
            FavoriteMatch.STR_TEAM_URL to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteMatch.TABLE_FAV, true)
        db.dropTable(FavoriteMatch.TABLE_ALERT, true)
        db.dropTable(FavoriteMatch.TABLE_TEAM, true)
    }

}

// Access property for Context
val Context.database: DatabaseSystem
    get() = DatabaseSystem.getInstance(applicationContext)