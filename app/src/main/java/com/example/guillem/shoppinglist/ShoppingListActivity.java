package com.example.guillem.shoppinglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private static final int MAX_BYTES = 1024;
    private ArrayList<ShoppingItem> itemList;
            private ArrayAdapter<ShoppingItem> adapter;
            private static final String FILENAME="shopping_list.txt";
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

                readItemList();

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

            @Override
            protected void onStop(){
                super.onStop();
                writeItemList();
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu){
                MenuInflater inflater =getMenuInflater();
                inflater.inflate(R.menu.options,menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item){
                switch (item.getItemId()){
                    case R.id.clear_checked:
                        clearChecked();
                        return true;
                    case R.id.clear_all:
                        clearAll();
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }

    private void clearChecked() {
        int i =0;
        while(i<itemList.size()){
            ShoppingItem item=itemList.get(i);
            if(item.isChecked()){
                itemList.remove(itemList.indexOf(item));
            }else i++;
        }
        adapter.notifyDataSetChanged();
    }

    private void clearAll(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.confirm_clear_all);
        builder.setPositiveButton(R.string.clear_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                itemList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(android.R.string.cancel,null);
        builder.create().show();

    }

    private void writeItemList(){
                 try {
                     FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                     for(ShoppingItem item:itemList){
                         String line= item.getText()+";"+item.isChecked()+"\n";
                         fos.write(line.getBytes());
                     }
                     fos.close();
                 } catch (FileNotFoundException e) {
                     Log.e("Guil","writeItemList:File not found exemption");
                     Toast.makeText(this,R.string.cannot_write,Toast.LENGTH_SHORT).show();
                 } catch (IOException e) {
                     Log.e("Guil","writeItemList:IO Exemption");
                     Toast.makeText(this,R.string.cannot_write,Toast.LENGTH_SHORT).show();
                 }
            }


             private void readItemList(){
                itemList=new ArrayList<>();
                 try {
                     FileInputStream fis =openFileInput(FILENAME);
                     byte[] buffer =new byte[MAX_BYTES];
                     int nread=fis.read(buffer);
                     if(nread>0) {
                         String content = new String(buffer, 0, nread);
                         String[] lines = content.split("\n");

                         for (String s : lines) {
                             String[] parts = s.split(";");
                                 itemList.add(new ShoppingItem(parts[0], parts[1].equals("true")));

                         }
                     }
                     fis.close();


                 } catch (FileNotFoundException e) {
                     Log.e("Guil","readItemList:File not found exemption");
                     Toast.makeText(this,R.string.cannot_read,Toast.LENGTH_SHORT).show();
                 } catch (IOException e) {
                     Log.e("Guil","readItemList:IO exemption");
                     Toast.makeText(this,R.string.cannot_read,Toast.LENGTH_SHORT).show();
                 }

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

