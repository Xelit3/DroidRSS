package com.droidrss.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
 
import java.util.ArrayList;
 
// TODO: Auto-generated Javadoc
/**
 * The Class ExpListAdapter.
 */
public class ExpListAdapter extends BaseExpandableListAdapter {
  
    /** The inflater. */
    private LayoutInflater inflater;
    
    /** The parent array of elements. */
    private ArrayList<ExpListParent> mParent;
 
    /**
     * Instantiates a new exp list adapter.
     *
     * @param context the context
     * @param expListParent the exp list parent
     */
    public ExpListAdapter(Context context, ArrayList<ExpListParent> expListParent){
        mParent = expListParent;
        inflater = LayoutInflater.from(context);
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    //counts the number of children items so the list knows how many times calls getArrayChild() method
    public int getChildrenCount(int i) {
        return mParent.get(i).getArrayChild().size();
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).getTitle();
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).getArrayChild().get(i1);
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
     */
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
 
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_parent, viewGroup,false);
        }
 
        TextView textView = (TextView) view.findViewById(R.id.lip_tv_title);
        textView.setTextColor(Color.BLACK);
        TextView tvCount = (TextView) view.findViewById(R.id.lip_tv_count);
        //"i" is the position of the parent/group in the list
        textView.setText(getGroup(i).toString());
        tvCount.setText(String.valueOf(mParent.get(i).getCount()));
        //return the entire view
        return view;
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
     */
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_child, viewGroup,false);
        }
 
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_child);
        //"i" is the position of the parent/group in the list and 
        //"i1" is the position of the child
        textView.setText(mParent.get(i).getArrayChild().get(i1).toString());
 
        //return the entire view
        return view;
    }
 
    /* (non-Javadoc)
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
 
    /* (non-Javadoc)
     * @see android.widget.BaseExpandableListAdapter#registerDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        /* used to make the notifyDataSetChanged() method work */
        super.registerDataSetObserver(observer);
    }
}
