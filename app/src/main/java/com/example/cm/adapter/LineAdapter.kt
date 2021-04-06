package com.example.cm.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cm.EcraAlterar
import com.example.cm.ListaNotas
import com.example.cm.R
import com.example.cm.criarnota

import com.example.cm.entities.Notas
import kotlinx.android.synthetic.main.recyclerline.view.*


const val TITULO = "nome"
const val DESC = "des"
const val ID = "id"

class LineAdapter internal constructor(
    context: Context,
    private val callbackInterface:CallbackInterface
) : RecyclerView.Adapter<LineAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Notas>() // Cached copy of cities

    interface CallbackInterface {
        fun passResultCallback(message: Int?)
    }

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.nome)
        val notaDescricao: TextView = itemView.findViewById(R.id.descricao)

        val button: ImageButton = itemView.findViewById(R.id.butaoeditar)
        val button2: ImageButton = itemView.findViewById(R.id.butaoeliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text =  current.nome
        holder.notaDescricao.text = current.descricao

        holder.button.setOnClickListener{
            val nome = holder.notaItemView.text
            val des = holder.notaDescricao.text
            val id = current.id
            val context = holder.notaItemView.context

            val intent = Intent(context, EcraAlterar::class.java).apply{
                putExtra(TITULO, nome )
                putExtra(DESC, des )
                putExtra( ID,id)
            }
            context.startActivity(intent)
        }

        holder.button2.setOnClickListener {
            val id = current.id
            callbackInterface.passResultCallback(id)
        }

    }

    internal fun setNotas(notas: List<Notas>) {
        this.notas = notas
        notifyDataSetChanged()
    }



    override fun getItemCount() = notas.size
}