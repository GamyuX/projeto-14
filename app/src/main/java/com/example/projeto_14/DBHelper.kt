package com.example.projeto_14

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context,"database.db",null,1){

        val sql = arrayOf(
            "CREATE TEBLE utilizador(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT,password TEXT)",
            "INSERT INTO utilizador(nome,password) VALUES('user','pass')",
            "INSERT INTO utilizador(nome,password) VALUES('admin','qwe')"
        )

        override fun onCreate(db: SQLiteDatabase) {
            sql.forEach {
                db.execSQL(it)
            }
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE utilizador")
        onCreate(db)
    }

    fun untilizadorInsert(nome: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome",nome)
        contentValues.put("password",password)
        db.insert("utilizador",null,contentValues)
        val res = db.insert("utilizador",null,contentValues)
        db.close()
        return res
    }

    fun untilizadorUpdate(id: Int, nome: String, password: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome",nome)
        contentValues.put("password",password)
        val res = db.update("utilizador",contentValues,"id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun untilizadorDelete(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("utilizador","id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun utilizadorSelectAll(): Cursor {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM utilizador",null)
        db.close()
        return c
    }

    fun utilizadorSelectById(id: Int): Cursor {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM utilizador WHERE id=?",arrayOf(id.toString()))
        db.close()
        return c
    }
    fun utilizadorObjectSelectByID(id: Int): Utilizador {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM utilizador WHERE id=?",arrayOf(id.toString()))
        var utilizador = Utilizador()
        if(c.count==1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val usernameIndex = c.getColumnIndex("username")
            val passwordIndex = c.getColumnIndex("password")

            val id = c.getInt(idIndex)
            val username = c.getString(usernameIndex)
            val password = c.getString(passwordIndex)

            utilizador = Utilizador(id,username,password)

        }
        db.close()
        return utilizador
    }

    fun listaUtilizadores(): ArrayList<Utilizador> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM utilizador",null)
        val listaUtilizadores: ArrayList<Utilizador> = ArrayList()
        if(c.count > 0){
            c.moveToFirst()
            do{
                val idIndex = c.getColumnIndex("id")
                val usernameIndex = c.getColumnIndex("username")
                val passwordIndex = c.getColumnIndex("password")

                val id = c.getInt(idIndex)
                val username = c.getString(usernameIndex)
                val password = c.getString(passwordIndex)
                listaUtilizadores.add(Utilizador(id,username,password))

            }while (c.moveToNext())
        }
        db.close()
        return listaUtilizadores
    }
}