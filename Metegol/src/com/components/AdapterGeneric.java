package com.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Generic Adapter that allows working with different view group. Use the Item
 * interface to display its contents.
 *
 * @author Carlos - InfinixSoft
 */

public class AdapterGeneric extends BaseAdapter {

    protected Context context;
    protected List<Item> itemsList;

    /**
     * Constructor
     *
     * @param context
     * @param itemsList List of Items
     */

    public AdapterGeneric(Context context, List<Item> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public int getCount() {
        return itemsList != null ? itemsList.size() : 0;
    }

    public Object getItem(int position) {
        return itemsList != null ? itemsList.get(position).getData() : null;
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * Return a view using the class that implements interface item.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        return itemsList != null ? itemsList.get(position).getView(convertView,
                context) : null;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

}
