package com.example.ajax.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MessagesFragment extends Fragment {

    private List<Message> messages;

    public MessagesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        messages = Arrays.asList(new Message("First name", "Hello", "Today"), new Message
                ("Another name", "Some text", "Yesterday"));

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ListView listView = (ListView) view.findViewById(R.id.messages_listview);
        MessageAdapter adapter = new MessageAdapter(getActivity(), messages);
        listView.setAdapter(adapter);
        return view;
    }
}
