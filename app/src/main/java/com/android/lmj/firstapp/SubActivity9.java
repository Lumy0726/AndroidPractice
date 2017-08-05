package com.android.lmj.firstapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.lmj.firstapp.tools.Tools;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

public class SubActivity9 extends AppCompatActivity {
    static final String FILENAME = "lmj.firstApp.saveFile";
    //System value.
    LayoutInflater inflater;
    //View
    LinearLayout sub9Container;
    TextView textView_Number;
    EditText editText_Str;
    //string number.
    int layoutStrNum;
    //IndexStr
    Vector<IndexStr> indexStrs = new Vector<IndexStr>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub9);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexStrs = new Vector<IndexStr>();
        sub9Container = (LinearLayout) findViewById(R.id.sub9_Container);
        textView_Number = (TextView) findViewById(R.id.textView_Number);
        editText_Str = (EditText) findViewById(R.id.editText_Str);
        Button button = (Button) findViewById(R.id.button_Confirm);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { onButton_Confirm(); }
        });
        button = (Button) findViewById(R.id.button36);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { save(); }
        });
        button = (Button) findViewById(R.id.button37);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { sendToSD(); }
        });
        load();
    }
    boolean getWriteExternalPer(){
        int permissionCheck;
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED){ return true; }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }

    void save(){
        //Save Str.
        File file = new File(getFilesDir(), FILENAME);
        file.deleteOnExit();
        FileOutputStream fOutS;
        ObjectOutputStream obOutS;
        try{
            file.createNewFile();
            fOutS = new FileOutputStream(file);
            obOutS = new ObjectOutputStream(fOutS);
            obOutS.writeObject(indexStrs);
            obOutS.close();
            Tools.simpleToast(getApplicationContext(), "Save complete");
        }
        catch (Exception e){
            Tools.simpleToast(getApplicationContext(), "Save fail");
        }
    }
    void load(){
        //recovery Str.
        FileInputStream fInS;
        ObjectInputStream obInS = null;
        try{
            fInS = new FileInputStream(new File(getFilesDir(), FILENAME));
            obInS = new ObjectInputStream(fInS);
            Object object = obInS.readObject();
            if (object instanceof Vector<?>){
                indexStrs = (Vector<IndexStr>) object;
                viewUpdate();
                Tools.simpleToast(getApplicationContext(), "Load Complete");
            }
            else throw new ClassNotFoundException();
        }
        catch (IOException e){ }
        catch (ClassNotFoundException e){
            Tools.simpleToast(getApplicationContext(), "Load fail");
        }
        finally {
            if (obInS != null){
                try { obInS.close(); } catch (IOException e){ }
            }
        }
    }
    void sendToSD(){
        //send string to sdCard.
        boolean isPermission = getWriteExternalPer();
        if (isPermission){
            String isWriteAble = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(isWriteAble)){
                File file = new File(Environment.getExternalStorageDirectory(), FILENAME);
                file.deleteOnExit();
                FileWriter fOutS;
                try{
                    file.createNewFile();
                    fOutS = new FileWriter(file);
                    for (int loop1 = 0, loop1End = indexStrs.size(); loop1 < loop1End; loop1++){
                        fOutS.write(indexStrs.elementAt(loop1).index + " ");
                        fOutS.write(indexStrs.elementAt(loop1).str);
                        fOutS.write("\n");
                    }
                    fOutS.close();
                    Tools.simpleToast(getApplicationContext(), "Save to SDCard complete");
                }
                catch (Exception e){
                    Tools.simpleToast(getApplicationContext(), "Save to SDCard fail");
                }
            }
            else {
                Tools.simpleToast(getApplicationContext(), "Cannot Write to External SDCard");
            }
        }
        else {
            Tools.simpleToast(getApplicationContext(), "Need Write to External SDCard Permission");
        }
    }
    public void onButton_Confirm(){
        String str;
        int index;
        //get String.
        str = editText_Str.getText().toString();
        if (str.equals("")){
            //void input.
            Tools.simpleToast(getApplicationContext(), "문자를 입력해 주세요.");
        }
        else {
            editText_Str.setText("");
            //layout inflate.
            layoutInflate(str);
            //add to indexStrs
            indexStrs.add(new IndexStr(layoutStrNum, str));
        }
    }
    public void onButton_Delete(View v){
        //get Index.
        int index = sub9Container.indexOfChild(v);
        //remove Layout.
        sub9Container.removeView(v);
        //
        layoutStrNum--;
        indexStrs.removeElementAt(index);
        //textView index update.
        TextView textView;
        for (int loop1 = index; loop1 < layoutStrNum; loop1++){
            textView = (TextView) sub9Container.getChildAt(loop1).findViewById(R.id.textView_Number);
            textView.setText("" + loop1);
        }
    }
    public void layoutInflate(String str){
        //layout inflate.
        inflater.inflate(R.layout.activity_sub9_list, sub9Container, true);
        Button button = (Button) sub9Container.getChildAt(layoutStrNum).findViewById(R.id.button_Delete);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View view = (View)v.getParent().getParent();
                onButton_Delete(view);
            }
        });
        //Set textView string.
        TextView textView = (TextView) sub9Container.getChildAt(layoutStrNum).findViewById(R.id.textView_Str);
        textView.setText(str);
        //Set textView index.
        textView = (TextView) sub9Container.getChildAt(layoutStrNum).findViewById(R.id.textView_Number);
        textView.setText("" + layoutStrNum);
        layoutStrNum++;
    }
    public void viewUpdate(){
        sub9Container.removeAllViewsInLayout();
        layoutStrNum = 0;
        for (int loop1 = 0, loop1End = indexStrs.size(); loop1 < loop1End; loop1++){
            layoutInflate(indexStrs.elementAt(loop1).str);
        }
    }
    public void onButton_Finish(View v){ finish(); }
}

class IntIndex implements Serializable{
    static final int DUMYINT = 0xaaaaaaaa;
    int index;
    IntIndex(){ this(0); }
    IntIndex(int index){ this.index = index; }
    //little cipher add.
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(index ^ DUMYINT);
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        index = in.readInt() ^ DUMYINT;
    }
}

class IndexStr extends IntIndex{
    String str = "";
    IndexStr(String str){ this(0, str); }
    IndexStr(int index){ this(index, ""); }
    IndexStr(int index, String str){
        super(index); this.str = str;
    }
    //little cipher add.
    private void writeObject(ObjectOutputStream out) throws IOException {
        for (int loop1 = 0, loop1End = str.length(); loop1 < loop1End; loop1++){
            out.writeChar(str.charAt(loop1) ^ IntIndex.DUMYINT);
        }
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        StringBuilder str = new StringBuilder();
        char input;
        try{
            while (true){
                input = (char)(in.readChar() ^ IntIndex.DUMYINT);
                str.append(input);
            }
        } catch (EOFException e){ }
        this.str = str.toString();
   }
}
