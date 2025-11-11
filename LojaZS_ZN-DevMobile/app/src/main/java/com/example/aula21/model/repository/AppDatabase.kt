package com.example.aula21.model.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aula21.model.dao.TodoDAO
import com.example.aula21.model.dao.UserDAO
import com.example.aula21.model.dao.CamisetaDAO
import com.example.aula21.model.dao.UsuarioDAO
import com.example.aula21.model.dao.PedidoDAO
import com.example.aula21.model.dao.PedidoItemDAO
import com.example.aula21.model.entity.Todo
import com.example.aula21.model.entity.User
import com.example.aula21.model.entity.Camiseta
import com.example.aula21.model.entity.Usuario
import com.example.aula21.model.entity.Pedido
import com.example.aula21.model.entity.PedidoItem
import com.example.aula21.utils.Helper

@Database(entities = [User::class, Todo::class, Camiseta::class, Usuario::class, Pedido::class, PedidoItem::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDAO(): UserDAO
    abstract fun getTodoDAO(): TodoDAO
    abstract fun getCamisetaDAO(): CamisetaDAO
    abstract fun getUsuarioDAO(): UsuarioDAO
    abstract fun getPedidoDAO(): PedidoDAO
    abstract fun getPedidoItemDAO(): PedidoItemDAO

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getDataBase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    Helper.DB_NAME
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }

        }
    }
}