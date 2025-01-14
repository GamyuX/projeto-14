package com.example.projeto_14

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_14.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<Utilizador>
    private lateinit var binding: ActivityMainBinding
    private var pos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        val listaUtilizadores = db.listaUtilizadores()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaUtilizadores)
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, position, _ ->
            binding.textId.setText("Id: ${listaUtilizadores[position].id}")
            binding.editUsername.setText(listaUtilizadores[position].username)
            binding.editPassword.setText(listaUtilizadores[position].password)
            pos = position
        }

        binding.buttonInsert.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            val res = db.utilizadorInsert(username,password)

            if(res > 0){
                Toast.makeText(applicationContext, "Usuario logado com sucesso $res".toString(), Toast.LENGTH_SHORT).show()
                listaUtilizadores.add(Utilizador(res.toInt(), username, password))
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(applicationContext, "Erro ao inserir: $res", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonEdit.setOnClickListener {
            if (pos >= 0) {
                val id = listaUtilizadores[pos].id
                val username = binding.editUsername.text.toString()
                val password = binding.editPassword.text.toString()

                val res = db.utilizadorUpdate(id, username, password)
                if (res > 0) {
                    Toast.makeText(
                        applicationContext,
                        "Usuario atualizado com sucesso $res".toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    listaUtilizadores[pos].username = username
                    listaUtilizadores[pos].password = password
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(applicationContext, "Erro ao atualizar: $res",Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.buttonDelete.setOnClickListener {
            if(pos >= 0) {
                val id = listaUtilizadores[pos].id
                var res = db.utilizadorDelete(id)
                if (res > 0) {
                    Toast.makeText(
                        applicationContext,
                        "Usuario removido com sucesso $res".toString(), Toast.LENGTH_SHORT
                    ).show()
                    listaUtilizadores.removeAt(pos)
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Erro ao apagar: $res",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}