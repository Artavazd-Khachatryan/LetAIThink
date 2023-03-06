package com.example.chatgptapi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatgptapi.R
import com.example.chatgptapi.model.ChatMode

class ChatModsAdapter : RecyclerView.Adapter<ChatModsAdapter.ChatModeViewHolder>() {

    private var chatModes = ArrayList<ChatMode>()

    var chatModeListener: OnChatModeListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatModeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_mode_item, parent, false)
        return ChatModeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatModeViewHolder, position: Int) {
        holder.bind(chatModes[position])
    }

    override fun getItemCount(): Int = chatModes.size

    fun updateMods(updatedMods: List<ChatMode>) {
        with(chatModes) {
            clear()
            addAll(updatedMods)
        }
        notifyDataSetChanged()
    }

    inner class ChatModeViewHolder(view: View) : ViewHolder(view) {

        private var chatMode: ChatMode? = null

        private val ivChatMode: ImageView = view.findViewById(R.id.ivChatMode)
        private val tvModeTitle: TextView = view.findViewById(R.id.tvModeTitle)
        private val clModeContainer: ConstraintLayout = view.findViewById(R.id.clModeContainer)

        init {
            view.setOnClickListener {
                chatModeListener?.onModeSelect(chatMode!!)
            }
        }

        fun bind(mode: ChatMode) {
            this.chatMode = mode

            with(mode) {
                ivChatMode.setImageResource(image)
                tvModeTitle.setText(title)

                if (selected) {
                    clModeContainer.setBackgroundResource(R.color.selected_chat_mode_color)
                } else {
                    clModeContainer.setBackgroundResource(android.R.color.white)
                }
            }
        }
    }

    fun interface OnChatModeListener {
        fun onModeSelect(mode: ChatMode)
    }
}