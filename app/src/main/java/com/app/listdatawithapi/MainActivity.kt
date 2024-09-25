package com.app.listdatawithapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: DataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel.data()
        val data = DataList(1,
        "1",
        "accusamus beatae ad facilis cum similique qui sunt",
        "https://via.placeholder.com/600/92c952",
        "https://via.placeholder.com/150/92c952")
        val array= arrayListOf(data,data)
        val adapter1 = DataListAdapter(array)
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this@MainActivity)

        recyclerView.adapter = adapter1
        viewModel.dataResult.observe(this){result ->
            Log.d("TAG", "onCreate: ${result.isSuccess}")
            Log.d("TAG", "onCreate: ${result.isFailure}")
            result?.onSuccess { response ->
                Log.d("TAG", "onCreate: ")
                response?.let {
                    Log.d("TAG", "onCreate: Data :: ${response.size}}")
                    adapter1.setList(response)

                }

            }?.onFailure {
                Log.d("TAG", "onCreate: ${it.message}")
                // Handle login error
                showToast("Something went wrong.")
            }


        }
    }
    private fun showToast(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}