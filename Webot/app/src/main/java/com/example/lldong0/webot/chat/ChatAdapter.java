package com.example.lldong0.webot.chat;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lldong0.webot.R;
import com.example.lldong0.webot.model.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int SELF = 100;
    private final int TEXT = 200;
    private final int OPTION2 = 302;
    private final int OPTION5 = 305;
    private final ChatClickListener listener;
    private ArrayList<Message> messageArrayList;

    public ChatAdapter(ArrayList<Message> messageArrayList, ChatClickListener listener) {
        this.messageArrayList = messageArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        int layoutResId = 0;
        if (viewType == SELF) {
            layoutResId = R.layout.chat_item_self;
        } else if (viewType == OPTION2) {
            layoutResId = R.layout.chat_select_options2;
        } else if (viewType == OPTION5) {
            layoutResId = R.layout.chat_select_options5;
        } else if (viewType == TEXT) {
            layoutResId = R.layout.chat_item_watson;
        }
        itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if (message.getId() != null && message.getId().equals("0")) {
            return SELF;
        }
        if (message.getId() != null && message.getId().equals("1")) {
            return TEXT;
        }
        if (message.getId() != null && message.getId().equals("2")) {
            return OPTION2;
        }
        if (message.getId() != null && message.getId().equals("5")) {
            return OPTION5;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        if (holder.getItemViewType() == OPTION2) {
            message.setTitle(message.getTitle());
            ((ViewHolder) holder).message.setText(message.getTitle());
            for (int i = 0; i < 2; i++) {
                ((ViewHolder) holder).buttons.get(i).setText(message.getLabels()[i]);
            }
        } else if (holder.getItemViewType() == OPTION5) {
            message.setTitle(message.getTitle());
            ((ViewHolder) holder).message.setText(message.getTitle());
            for (int i = 0; i < 5; i++) {
                ((ViewHolder) holder).buttons.get(i).setText(message.getLabels()[i]);
            }
        } else {
            message.setMessage(message.getMessage());
            ((ViewHolder) holder).message.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.message)
        TextView message;
        @BindViews({
                R.id.btn_options1, R.id.btn_options2, R.id.btn_options3, R.id.btn_options4, R.id.btn_options5
        })
        @Nullable
        List<Button> buttons;

        private WeakReference<ChatClickListener> listenerRef;

        public ViewHolder(View view, ChatClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            for (Button item : buttons) {
                if (item != null) item.setOnClickListener(this);
            }

            listenerRef = new WeakReference<>(listener);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onClick(v);
        }
    }

}