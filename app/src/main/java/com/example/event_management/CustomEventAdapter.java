package com.example.event_management;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class CustomEventAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> values;

    public CustomEventAdapter(@NonNull Context context, @NonNull ArrayList<Event> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.layout_event_row, parent, false);

        TextView eventName = rowView.findViewById(R.id.tvEventName);
        TextView eventDateTime = rowView.findViewById(R.id.tvEventDateTime);
        TextView eventPlaceName = rowView.findViewById(R.id.tvEventPlaceName);
        //TextView eventType = rowView.findViewById(R.id.tvEventType);

        Event e = values.get(position);
        eventName.setText(e.name);
        eventDateTime.setText(e.datetime);
        eventPlaceName.setText(e.place);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainActivity.class);
                intent.putExtra("key", e.key);
                context.startActivity(intent);
            }
        });

        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog("Do you want to delete this event?","Delete Event","Yes","No");
                return true;
            }
        });
        return rowView;
    }
    private void showDialog(String message, String title, String btn1,String btn2){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);

        builder.setCancelable(false)
                .setPositiveButton(btn1,new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        //Util.getInstance().deleteByKey(MainActivity.this,key);
                        //dialog.cancel();
                        //loadData();
                        //adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert=builder.create();
        alert.show();
    }
}
