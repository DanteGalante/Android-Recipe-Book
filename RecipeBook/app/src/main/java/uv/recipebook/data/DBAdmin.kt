package uv.recipebook.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBAdmin(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase){
        db.execSQL("create table recetas (id int primary key, imagen Blob, titulo text, descripcion text, ingredientes text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldversion:Int, newversion: Int) {

    }
}