package com.example.pierwszawersjaapki.Pomoc;

import android.content.Context;
import android.media.MediaDrm;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pierwszawersjaapki.R;

import java.util.HashMap;
import java.util.List;

public class PomocAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final List<String> pytania;
    private final HashMap<String, String> odpowiedzi;

    public PomocAdapter(Context context, List<String> pytania, HashMap<String, String> odpowiedzi) {
        this.context = context;
        this.pytania = pytania;
        this.odpowiedzi = odpowiedzi;
    }

    // group - pytania
    // children - odpowiedzi

    @Override
    public int getGroupCount() {
        return pytania.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pytania.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return odpowiedzi.get(pytania.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String pytanie = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pomoc_pytanie, parent,false);
        }

        TextView tv_pytanie = convertView.findViewById(R.id.tv_group);
        ImageView iv_arrow = convertView.findViewById(R.id.iv_arrow);

        tv_pytanie.setText(pytanie);

        if (isExpanded) {
            iv_arrow.setImageResource(R.drawable.ic_arrow_up);
        } else {
            iv_arrow.setImageResource(R.drawable.ic_arrow_down);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String odpowiedz = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pomoc_odpowiedz, parent, false);
        }
        TextView tv_odpowiedz = convertView.findViewById(R.id.tv_child);

        tv_odpowiedz.setText(odpowiedz);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
