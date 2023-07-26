package com.example.training.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.training.R;
import com.example.training.UserDetails;

import java.util.HashMap;
import java.util.List;

public class ExpandableListViewClass extends BaseExpandableListAdapter {

    private Context context;
    private  List<String> chapterlist;
    private  List<String> originalist;
    private List<String> filteredGroupList;
    private HashMap<String, List<UserDetails>> topicslist;
    private HashMap<String, List<UserDetails>> filteredChildMap;


    public ExpandableListViewClass(Context context, List<String> chapterlist, HashMap<String, List<UserDetails>> topicslist) {
        this.context = context;
        this.chapterlist = chapterlist;
        this.topicslist = topicslist;
        this.filteredGroupList = chapterlist;
        this.filteredChildMap = topicslist;
    }

    @Override
    public int getGroupCount() {
        //return  filteredGroupList.size();
        return this.chapterlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //String group = filteredGroupList.get(groupPosition);
        //return filteredChildMap.get(group).size();
        return this.topicslist.get(this.chapterlist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        //return filteredGroupList.get(groupPosition);
        return this.chapterlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
       //String group = filteredGroupList.get(groupPosition);
        //return filteredChildMap.get(group).get(childPosition);
        return this.topicslist.get(this.chapterlist.get(groupPosition)).get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String chapterTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chapter_list,null);

        }
        TextView chapterTv = convertView.findViewById(R.id.chapter_tv);
        chapterTv.setText(chapterTitle);
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // Get the UserDetails object from the topicslist
        UserDetails userDetails = (UserDetails) getChild(groupPosition, childPosition);

        // Check if convertView is null and inflate the layout if needed
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.topics_list, null);
        }

        // Now you can access the properties of the UserDetails object and set them to the views in your layout
        TextView userNameTextView = convertView.findViewById(R.id.topics_tv);
        TextView versionDateTextView = convertView.findViewById(R.id.version);

        // Set the values from UserDetails to the TextViews
        userNameTextView.setText(userDetails.getUserName());
        versionDateTextView.setText(userDetails.getVersionDate());

        return convertView;
    }


   /* @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String topicsTitle = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.topics_list, null);

        }
        ImageView image = convertView.findViewById(R.id.imageView2);
        TextView topicsTV = convertView.findViewById(R.id.topics_tv);
        topicsTV.setText(topicsTitle);
        String ver = (String) getChild(groupPosition, childPosition);

        //System.out.println(ver);
        if (ver.matches("[0-9]{2}.[0-9]{2}.[0-9]{4}")) {
            image.setImageResource(R.mipmap.ic_version_date);
        } else {
            if (ver.matches("\\d+(?:\\.\\d+)?.")) {
                image.setImageResource(R.mipmap.ic_version);
                //System.out.println("true");
            } else {
                image.setImageResource(R.mipmap.ic_user_round);
                //System.out.println("false");
            }
        }
        return convertView;
    }

    */

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

