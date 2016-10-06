package com.example.ajax.myapplication;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class MessageAdapter extends ArrayAdapter<Message> {
    private Activity context;
    private List<Message> messages;


    MessageAdapter(Activity context, List<Message> messages) {
        super(context, R.layout.message_listview_item, messages);
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.message_listview_item, null, true);
            holder = new ViewHolder();
            holder.companimName = (TextView) rowView.findViewById(R.id.companion_name);
            holder.message = (TextView) rowView.findViewById(R.id.message);
            holder.lastSeen = (TextView) rowView.findViewById(R.id.last_seen);
            holder.imageView = (RoundImageView) rowView.findViewById(R.id.companion_image);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.companimName.setText(messages.get(position).getName());
        holder.message.setText(messages.get(position).getMessage());
        holder.lastSeen.setText(messages.get(position).getLastSeen());
        holder.imageView.setImageResource(R.drawable.profile_image);
        return rowView;
    }

    private static class ViewHolder {
        RoundImageView imageView;
        TextView companimName;
        TextView message;
        TextView lastSeen;
    }
}
