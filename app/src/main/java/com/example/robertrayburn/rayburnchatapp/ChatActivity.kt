package com.example.robertrayburn.rayburnchatapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.robertrayburn.rayburnchatapp.CellData
import com.example.robertrayburn.rayburnchatapp.CellViewAdapter
import com.example.robertrayburn.rayburnchatapp.ChatData
import com.example.robertrayburn.rayburnchatapp.FireChatService
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        var ab = getSupportActionBar()
        ab?.setTitle("Chat Project")
        ab?.setSubtitle("Chat Room")

        ab?.setDisplayHomeAsUpEnabled(true)

        if(this.intent.hasExtra("userEmail")) {
            mUserEmail = this.intent.getStringExtra("userEmail")
            mUserImageUrl = this.intent.getStringExtra("userImageUrl")

        }
        else{
            Log.w("debug","Activity requires a logged in User")
        }

        attachRecyclerView()

        chatService.setupService(chatRecyclerView.context,{ message -> addMessageToRecyclerView(message)})

        sendButton.setOnClickListener({ view -> sendMessage()})
    }

    private var mUserEmail:String = ""
    private var mUserImageUrl:String = ""


    private val chatService = FireChatService.instance

    private fun sendMessage(){
        chatService.sendMessage(mUserEmail,mUserImageUrl,sendText.text.toString())
    }

    private fun addMessageToRecyclerView(message: ChatData?){
        if(message != null){
            val cellData = CellData(message.fromEmail,message.fromImageURL,message.message)
            addCelltoRecyclerView(cellData)
            sendText.setText("")
        }
    }

    lateinit var adapter: CellViewAdapter

    private fun attachRecyclerView(){
        val manager = LinearLayoutManager(this)
        chatRecyclerView.setHasFixedSize(true)
        chatRecyclerView.layoutManager = manager
        chatRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        initializeRecyclerView()
    }


    private fun initializeRecyclerView() {
        adapter = CellViewAdapter{ view, position -> rowTapped(position)}
        chatRecyclerView.adapter = adapter
    }

    private fun rowTapped(position: Int){
        Log.d("debug", adapter.rows[position].headerTxt + " " + adapter.rows[position].messageText)
    }

    private fun addCelltoRecyclerView(cellData: CellData) {
        adapter.addCellData(cellData)
        chatRecyclerView.smoothScrollToPosition(adapter.getCellCount() - 1)
    }
}
