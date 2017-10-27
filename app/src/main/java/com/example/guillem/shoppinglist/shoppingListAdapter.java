package com.example.guillem.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by sakum on 27/10/2017.
 */

public class shoppingListAdapter extends ArrayAdapter<ShoppingItem> {
    public shoppingListAdapter(Context context, int resource,  List objects) {
        super(context, resource, objects);

    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View result=convertView;
        if(result==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            result=inflater.inflate(R.layout.shopping_item,null);

        }
        CheckBox shopping_item =(CheckBox) result.findViewById(R.id.shopping_item);
        ShoppingItem item =getItem(position);
        shopping_item.setText(item.getText());
        shopping_item.setChecked(item.isChecked());
        return result;
    }
}
