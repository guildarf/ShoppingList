package com.example.guillem.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

            private ArrayList<ShoppingItem> itemList;
            private ArrayAdapter<ShoppingItem> adapter;

            private ListView list;
            private Button btn_add;
            private EditText edit_item;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_shopping_list);

                list = (ListView) findViewById(R.id.list);
                btn_add = (Button) findViewById(R.id.btn_add);
                edit_item = (EditText) findViewById(R.id.insertItem);

                itemList=new ArrayList<>();
                itemList.add(new ShoppingItem("Fuet"));
                itemList.add(new ShoppingItem("Jamon"));
                itemList.add(new ShoppingItem("Pan"));
                itemList.add(new ShoppingItem("Tomate"));

                adapter = new shoppingListAdapter(
                        this,
                        R.layout.shopping_item,
                        itemList
                 );

                edit_item.setOnEditorActionListener(new TextView.OnEditorActionListener(){
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        addItem(null);
                        return true;
                    }
                });


                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        itemList.get(i).togleCheched();
                        adapter.notifyDataSetChanged();
                    }
                });

                list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterlistView, View item, int pos, long id) {
                        maybeRemoveItem(pos);
                        return true;
                    }
                });
         }

    private void maybeRemoveItem(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(getResources().getString(R.string.confirm_message)  +" '"+itemList.get(pos).getText()+"' ?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemList.remove(pos);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel,null);
        builder.create().show();


    }

    public void addItem(View view) {
        String item_text=edit_item.getText().toString();
        if(!item_text.isEmpty()){
            itemList.add(new ShoppingItem(item_text));
            adapter.notifyDataSetChanged();
            edit_item.setText("");
        }
        list.smoothScrollToPosition(itemList.size()-1);

    }
}

