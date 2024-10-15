package com.aml.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aml.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() { //for room this will be always abstarct class
    abstract fun getArticleDao(): ArticleDao // Room with do automatically its implementation behind the scene for us

    companion object{
        @Volatile // other threads will immediately see when a thread change this instance
        private var instance: ArticleDatabase?=null
        //make sure their is on single instance of this variable
        private val LOCK = Any() // we will use this to synchronize setting the instance

        //ensure only single initialization
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
                ).build()
    }
}