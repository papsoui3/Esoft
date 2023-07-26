package com.example.training.ui;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.training.R;
import com.example.training.UserDetails;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class fragmentcompany extends AppCompatActivity {

    ExpandableListViewClass expandableListAdapter;

    ExpandableListView expandableListView;
    
    List<String> chapterlist;
    HashMap<String,List<UserDetails>> topicslist;
    List<String> orginalchapterlist = new ArrayList<>();
    HashMap<String,List<UserDetails>> originaltopicslist = new HashMap<>();
    private EditText searchEditText;
    List<String> listWithoutDuplicates ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_company);



        searchEditText = findViewById(R.id.et_search);
       expandableListView = findViewById(R.id.elistview);

       showlist();
        HashSet<String> set = new HashSet<>(chapterlist);
        listWithoutDuplicates.addAll(set);


        expandableListAdapter = new ExpandableListViewClass(this,listWithoutDuplicates,topicslist);



        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                ImageButton remove_btn = findViewById(R.id.removeImageButton);
                remove_btn.setVisibility(View.GONE);
            }
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ImageButton remove_btn = findViewById(R.id.removeImageButton);
                remove_btn.setVisibility(View.VISIBLE);
                remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemType = ExpandableListView.getPackedPositionType(id);
                        int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                        int childPosition = ExpandableListView.getPackedPositionChild(id);

                        // Check if the item is a child item and not a group header
                        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                            // Retrieve the group key (e.g., "Group 1")
                            String groupKey = (String) expandableListAdapter.getGroup(groupPosition);

                            // Retrieve the list associated with the group key from the hashmap
                            List<UserDetails> groupList = topicslist.get(groupKey);

                            // Remove the item from the list
                            if (groupList != null && childPosition != -1 && childPosition < groupList.size()) {
                                groupList.remove(childPosition);
                                expandableListAdapter.notifyDataSetChanged();
                                remove_btn.setVisibility(View.GONE);

                            }

                            // Handle the long click event

                        }
                    }
                });
                return true;
            }
        });
        orginalchapterlist.addAll(listWithoutDuplicates);
        originaltopicslist.putAll(topicslist);

        expandableListView.setAdapter(expandableListAdapter);

        searchEditText.addTextChangedListener(textWatcher);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used in this example
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Update the expandable list based on the search text
            filterExpandableList(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not used in this example

        }
    };


    private void filterExpandableList(String searchText) {
        // Clear the previous data in the expandable list
        //chapterlist.clear();
        listWithoutDuplicates.clear();

        topicslist = new HashMap<>();
        expandableListView.setAdapter(expandableListAdapter);
        //System.out.println(orginalchapterlist);
        // Add filtered data to the expandable list based on the search text
        for (String title : orginalchapterlist) {
            List<UserDetails> filteredItems = new ArrayList<>();
            for (UserDetails item : Objects.requireNonNull(originaltopicslist.get(title))) {
                if (title.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
           // System.out.println(filteredItems);
            if (!filteredItems.isEmpty()) {
                listWithoutDuplicates.add(title);
                topicslist.put(title, filteredItems);
            }
           // System.out.println(topicslist);

        }

        // Notify the adapter that the data has changed
        expandableListAdapter.notifyDataSetChanged();

    }




    private void showlist() {
        chapterlist = new ArrayList<String>();
        topicslist = new HashMap<String, List<UserDetails>>();
        listWithoutDuplicates = new ArrayList<>();

        Intent intent = getIntent();
        ArrayList<String> stringList = intent.getStringArrayListExtra("dataList");
        ArrayList<String> stringnamelist = intent.getStringArrayListExtra("dataList_names");
        ArrayList<String> version = intent.getStringArrayListExtra("version");
        ArrayList<String> version_date = intent.getStringArrayListExtra("version_date");

        ArrayList<String> sales_name = intent.getStringArrayListExtra("sales");

        Integer c = intent.getIntExtra("counter",0);


        if (intent != null) {

            //System.out.println(stringList);
            //System.out.println(c);
            if (stringList != null) {
                // Process the stringList
            } else {
                // Handle the case when the extra is not available
            }
        } else {
            // Handle the case when the intent is null
        }

        chapterlist.add(stringnamelist.get(0));
        List<UserDetails> customer1 = new ArrayList<>();
        customer1.add(new UserDetails(sales_name.get(0),version_date.get(0)));
        topicslist.put(chapterlist.get(0),customer1);
        for (int i=1;i<sales_name.size();i++) {
            chapterlist.add(stringnamelist.get(i));
            if (topicslist.containsKey(chapterlist.get(i))){
                for (Map.Entry<String, List<UserDetails>> entry : topicslist.entrySet()) {
                    String key = entry.getKey();
                    if (Objects.equals(key, chapterlist.get(i))){
                        List<UserDetails> value = entry.getValue();
                        value.add(new UserDetails(sales_name.get(i),version_date.get(i)));
                        topicslist.put(chapterlist.get(i),value);
                    }
                }

            }else {
                List<UserDetails> customer = new ArrayList<>();
                customer.add(new UserDetails(sales_name.get(i),version_date.get(i)));
                topicslist.put(chapterlist.get(i),customer);
            }


            /*
            List<String> topic1 = new ArrayList<>();

            topic1.add(stringnamelist.get(i));
            topic1.add(version.get(i));
            topic1.add(version_date.get(i));
            */


        }

    }


}
