package com.example.lldong0.webot.chat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lldong0.webot.MainActivity;
import com.example.lldong0.webot.R;
import com.example.lldong0.webot.helper.CreateDialogHelper;
import com.example.lldong0.webot.home.HomeFragment;
import com.example.lldong0.webot.model.Message;
import com.example.lldong0.webot.model.parse.Generic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    com.ibm.watson.developer_cloud.assistant.v1.model.Context context = null;
    private final int RESPONSE_NUM = 10;
    private final int OPTION_NUM = 10;
    private boolean initialRequest;
    private ChatAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private String[] labels;
    private AlertDialog alertDialog;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.message)
    EditText inputMessage;
    @BindView(R.id.btn_send)
    ImageView btnSend;
    @BindView(R.id.chat_toolbar)
    Toolbar chatToolbar;
    @BindView(R.id.chat_toolbar_title)
    TextView chatToolbarTitle;
    @BindView(R.id.chat_info)
    ImageButton btnChatInfo;
    @BindView(R.id.insert_note_btn)
    ImageView insertNoteBtn;
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
//        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        ButterKnife.bind(this);

        initChat();
        setupToolbar();
        setupRecyclerView();

    }

    @OnClick(R.id.btn_send)
    public void send() {
        if (checkInternetConnection())
            sendMessage();
    }

    private void initChat() {
        labels = new String[OPTION_NUM];
        this.inputMessage.setText("");
        this.initialRequest = true;
        sendMessage();
    }

    private void setupToolbar() {
        // toolbar
        setSupportActionBar(chatToolbar);
        chatToolbarTitle.setText("위봇");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatToolbar.setNavigationOnClickListener((v) -> {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            finish();
        });
    }

    @OnClick(R.id.chat_info)
    public void btnChatInfo(View v) {
        View alertView = View.inflate(ChatActivity.this, R.layout.chat_info, null);
        alertDialog = new AlertDialog.Builder(ChatActivity.this)
                .setView(alertView)
                .create();
        alertDialog.show();
    }

    @OnClick(R.id.insert_note_btn)
    public void insertNote() {
        CreateDialogHelper createDialogHelper = new CreateDialogHelper();
        if (createDialogHelper.loginDialog(ChatActivity.this)) {
            WriteNoteFragment writeNoteFragment = new WriteNoteFragment();
            writeNoteFragment.show(getSupportFragmentManager(), writeNoteFragment.getTag());
        }

    }

    public void chatInfoClear(View v) {
        alertDialog.dismiss();
    }

    private void setupRecyclerView() {
        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(messageArrayList, view -> {
            switch (view.getId()) {
                case R.id.btn_options1:
                    ChatActivity.this.inputMessage.setText(labels[0]);
                    sendMessage();
                    break;
                case R.id.btn_options2:
                    ChatActivity.this.inputMessage.setText(labels[1]);
                    sendMessage();
                    break;
                case R.id.btn_options3:
                    ChatActivity.this.inputMessage.setText(labels[2]);
                    sendMessage();
                    break;
                case R.id.btn_options4:
                    ChatActivity.this.inputMessage.setText(labels[3]);
                    sendMessage();
                    break;
                case R.id.btn_options5:
                    ChatActivity.this.inputMessage.setText(labels[4]);
                    sendMessage();
                    break;
                default:
                    break;
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    private void sendMessage() {

        final String strInputMessage = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(strInputMessage);
            inputMessage.setId("0");
            messageArrayList.add(inputMessage);
            Log.i("input message", strInputMessage);
        } else {
            Message inputMessage = new Message();
            inputMessage.setMessage(strInputMessage);
            inputMessage.setId("1");
            this.initialRequest = false;
            Log.i("init watson", "왓슨 인사 말 받아오기.");
        }

        this.inputMessage.setText("");
        try {
            mAdapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
            try {

                Assistant service = new Assistant("2018-09-20");
                service.setUsernameAndPassword(watsonUsername, watsonPassword);
                InputData input = new InputData.Builder(strInputMessage).build();
                Log.i("input Message Text", strInputMessage);
                MessageOptions options = new MessageOptions
                        .Builder(workspacesId)
                        .input(input)
                        .context(context)
                        .build();
                MessageResponse response = service.message(options).execute();

                if (response.getContext() != null) {
                    context = response.getContext();
                }
                Log.i("context", context.toString());
                Log.i("response", response.toString());

                if (response.getOutput() != null) {
                    ArrayList responseList = (ArrayList) response.getOutput().get("text");
                    Log.i("responseList Text", responseList.toString());

                    Message[] outMessage = new Message[RESPONSE_NUM];
                    if (responseList.size() > 0) {
                        for (int i = 0; i < responseList.size(); i++) {
                            outMessage[i] = new Message();
                            outMessage[i].setMessage(responseList.get(i).toString());
                            outMessage[i].setId("1");
                            Log.i("outMessage", responseList.get(i).toString());
                            messageArrayList.add(outMessage[i]);
                        }
                    } else {
                        String strGeneric = response.getOutput().getGeneric().toString();
                        Log.i("repsonse generic", strGeneric);

                        List<Generic> parseList = new Gson().fromJson(strGeneric, new TypeToken<List<Generic>>() {
                        }.getType());
                        Log.i("pare list title", parseList.get(0).getTitle());

                        String cntOption = null;

                        for (int i = 0; i < parseList.get(0).getOptions().size(); i++) {
                            labels[i] = parseList.get(0).getOptions().get(i).getLabel();
                        }
                        if (parseList.get(0).getOptions().size() == 2) {
                            cntOption = "2";
                        }
                        if (parseList.get(0).getOptions().size() == 5) {
                            cntOption = "5";
                        }
                        for (int i = 0; i < parseList.size(); i++) {
                            outMessage[i] = new Message();
                            outMessage[i].setTitle(parseList.get(i).getTitle());
                            outMessage[i].setLabels(labels);
                            outMessage[i].setId(cntOption);
                            messageArrayList.add(outMessage[i]);
                        }


                    }
                }

                runOnUiThread(() -> {
                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.getItemCount() > 1) {
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null)
            activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            return true;
        } else {
            Toast.makeText(this, " 인터넷 연결을 확인해주세요. ", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        HomeFragment.newInstance();
        startActivity(intent);
        finish();
    }
}

