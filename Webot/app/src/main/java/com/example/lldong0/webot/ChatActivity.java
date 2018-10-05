package com.example.lldong0.webot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lldong0.webot.model.Message;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    com.ibm.watson.developer_cloud.assistant.v1.model.Context context = null;
    private final int RESPONSE_NUM = 10;
    private boolean initialRequest;
    private ChatAdapter mAdapter;
    private ArrayList messageArrayList;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    EditText inputMessage;
    @BindView(R.id.btn_send)
    ImageView btnSend;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView chatToolbarTitle;
    @BindString(R.string.waston_assistant_workspacesId)
    String workspacesId;
    @BindString(R.string.watson_assistant_username)
    String watsonUsername;
    @BindString(R.string.watson_assistant_password)
    String watsonPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        ButterKnife.bind(this);
        // toolbar
        setSupportActionBar(toolbar);
        chatToolbarTitle.setText("위봇");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 뒤로가기 이벤트
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, LoginActivity.class));
                finish();
            }
        });

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(messageArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.inputMessage.setText("");
        this.initialRequest = true;
        sendMessage();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection()) {
                    sendMessage();
                }
            }
        });
    }

    // Sending a message to Watson Conversation Service
    private void sendMessage() {

        final String strInputMessage = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(strInputMessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
        } else {
            // set self id
            Message inputMessage = new Message();
            inputMessage.setMessage(strInputMessage);
            inputMessage.setId("100");
            this.initialRequest = false;
        }

        this.inputMessage.setText("");
        mAdapter.notifyDataSetChanged();

        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {

                    Assistant service = new Assistant("2018-09-20");
                    service.setUsernameAndPassword(watsonUsername, watsonPassword);
                    com.ibm.watson.developer_cloud.assistant.v1.model.InputData input = new com.ibm.watson.developer_cloud.assistant.v1.model.InputData.Builder(strInputMessage).build();
                    Log.i("input Message Text", strInputMessage);
                    com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions options = new com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions
                            .Builder(workspacesId)
                            .input(input)
                            .context(context)
                            .build();
                    com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse response = service.message(options).execute();

                    //Passing Context of last conversation
                    if (response.getContext() != null) {
                        context = response.getContext();
                    }
                    Log.i("context", context.toString());
                    Log.i("response", response.toString());

                    Message[] outMessage = new Message[RESPONSE_NUM];

                    if (response != null) {
                        if (response.getOutput() != null && response.getOutput().containsKey("text")) {
                            ArrayList<String> responseList = (ArrayList) response.getOutput().get("text");
                            Log.i("responseList Text", responseList.toString());

                            if (responseList != null && responseList.size() > 0) {
                                for (int i = 0; i < responseList.size(); i++) {
                                    outMessage[i] = new Message();
                                    outMessage[i].setMessage(responseList.get(i));
                                    outMessage[i].setId("2");
                                    Log.i("outMessage", responseList.get(i));
                                    messageArrayList.add(outMessage[i]);
                                }
                            }
                        }

                        runOnUiThread(new Runnable() {
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                if (mAdapter.getItemCount() > 1) {
                                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Check for network connections
        if (isConnected) {
            return true;
        } else {
            Toast.makeText(this, " 인터넷 연결을 확인해주세요. ", Toast.LENGTH_LONG).show();
            return false;
        }

    }
}

