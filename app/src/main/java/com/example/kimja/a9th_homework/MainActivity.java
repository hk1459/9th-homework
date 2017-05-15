package com.example.kimja.a9th_homework;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    LinearLayout linear1, linear2;
    ListView listView;
    EditText et;
    DatePicker datePicker;
    Button btnsave;
    TextView tvCount;

    ArrayAdapter adapter;
    ArrayList<String> list= new ArrayList<String>();
    ArrayList<data> datas = new ArrayList<data>();

    int deleteposition , deletelist;
    Calendar canlendar = new GregorianCalendar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkpermission();
        init();
    }
    void checkpermission(){
        int permissioninfo = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissioninfo == PackageManager.PERMISSION_GRANTED){ //권한이 이미 허용 됬는지 확인
            Toast.makeText(getApplicationContext(),
                    "SD Card 쓰기 권한 있음",Toast.LENGTH_SHORT).show();
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),
                        "권한이 없으면 일기를 등록 할 수 없습니다.",Toast.LENGTH_SHORT).show();
                //한번 거부하면 실행하는 부분.
                //권한 재요청
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        String str = null;
        if (requestCode == 100){
            if(grantResults.length >0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
                str = "SD Card 쓰기권한 승인";
            else str = "SD Card 쓰기권한 거부";
            Toast.makeText(this, str
                    ,Toast.LENGTH_SHORT).show();
        }
    }
    public String getExternalPath(){  //ppt 부연설명 // 외부 메모리 파일처리를 위해 꼭 만들어야 함
        String sdPath = "";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)) {
            sdPath =
                    Environment.getExternalStorageDirectory ().getAbsolutePath() + "/";
//sdPath = "/mnt/sdcard/";
        }else
            sdPath = getFilesDir() + "";
//        Toast.makeText(getApplicationContext(),
//                sdPath, Toast.LENGTH_SHORT).show();
        return sdPath;
    }

    void onClick(View v){
        if(v.getId() == R.id.btn1){ //일기 등록
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);
        }
        else if (v.getId() == R.id.btnsave){ //등록 및 수정
            if(btnsave.getText().equals("수정")){
                //원래 수정했던 파일을 삭제하고 새로 만듬.
                String path = getExternalPath();
                path = path + "/diary";
                File file = new File(path+"/"+list.get(deletelist));
                file.delete();
                list.remove(deletelist);
                datas.remove(deleteposition);
                //datepicker
                String selectyear = Integer.toString(datePicker.getYear());
                String selectmonth = Integer.toString(datePicker.getMonth());
                String selectdate = Integer.toString(datePicker.getDayOfMonth());
                datas.add(new data(selectyear,selectmonth,selectdate,et.getText().toString()));
                String select = datas.get(datas.size()-1).toString();

                //외부 메모리 쓰기
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/"+
                            select , true));
                    bw.write(et.getText().toString());
                    bw.close();
                    Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage() + ":" + getFilesDir(),
                            Toast.LENGTH_SHORT).show();
                }
                //추가
                list.add(select);
                adapter.notifyDataSetChanged();
                btnsave.setText("저장");
                adapter.sort(m_comparator);
                //초기화
                et.setText("");

                //
                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);

            } else {
                //등록을 눌렀을 때 디렉토리 생성

                String path = getExternalPath();
                path = path + "/diary";
                File file = new File(path);
                String msg ="";

                if(file.isDirectory() == false){
                    file.mkdir();
                    msg = "디렉토리 생성";

                }else {
                    msg = "디렉토리가 이미 존재합니다.";
                }
                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();

                //datepicker
                String selectyear = Integer.toString(datePicker.getYear());
                String selectmonth = Integer.toString(datePicker.getMonth());
                String selectdate = Integer.toString(datePicker.getDayOfMonth());
                datas.add(new data(selectyear,selectmonth,selectdate,et.getText().toString()));
                String select = datas.get(datas.size()-1).toString();

                //외부 메모리 쓰기
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path + "/"+
                            select , true));
                    bw.write(et.getText().toString());
                    bw.close();
                    Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.getMessage() + ":" + getFilesDir(),
                            Toast.LENGTH_SHORT).show();
                }
                //추가
                list.add(select);
                adapter.sort(m_comparator);
                adapter.notifyDataSetChanged();
                tvCount.setText("등록된 메모 갯수 : "+Integer.toString(datas.size()));

                //초기화 +datepicker 도
                et.setText("");
                datePicker.updateDate(canlendar.get(Calendar.YEAR),canlendar.get(Calendar.MONTH)
                        ,canlendar.get(Calendar.DAY_OF_MONTH));

                linear2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
            }
        }
       else if (v.getId() == R.id.btncancel){ //등록 취소
            if(btnsave.getText().equals("수정")) {
                btnsave.setText("저장");
            }
            //초기화
            et.setText("");
            datePicker.updateDate(canlendar.get(Calendar.YEAR),canlendar.get(Calendar.MONTH)
                    ,canlendar.get(Calendar.DAY_OF_MONTH));

            linear2.setVisibility(View.GONE);
            linear1.setVisibility(View.VISIBLE);
        }
//        else if (v.getId() == R.id.button){ //그냥 확인용 나중에 지울 것
//            String path = getExternalPath();
//            path = path + "diary";
//            File[] files = new File(path).listFiles();
//
//            String str = "";
//            for(File f:files){
//                str += f.getName() + "\n" ;
//            }
//            Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
//        }
    }

    void init(){
        linear1 = (LinearLayout)findViewById(R.id.linear1);
        linear2 = (LinearLayout)findViewById(R.id.linear2);
        et = (EditText)findViewById(R.id.et);
        datePicker = (DatePicker)findViewById(R.id.datepicker);
        listView  =  (ListView)findViewById(R.id.listview);
        btnsave = (Button)findViewById(R.id.btnsave);
        tvCount = (TextView)findViewById(R.id.tvCount);


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제")
                        .setMessage("항목을 삭제하시겠습니까?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //파일도 지우기
                                String path = getExternalPath();
                                path = path + "/diary";
                                File file = new File(path +"/"+ list.get(position));
                                file.delete();
                                for(int i = 0 ; i <list.size();i++){
                                    if(list.get(position) == datas.get(i).toString()){
                                        deleteposition = i;
                                        break;
                                    }
                                }
                                //항목 지우기
                                list.remove(position);
                                datas.remove(deleteposition);
                                tvCount.setText("등록된 메모 갯수 : "+Integer.toString(datas.size()));
                                adapter.sort(m_comparator);
                                adapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("Cancel",null)
                        .show();
                adapter.notifyDataSetInvalidated();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //화면 전환
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.VISIBLE);
                btnsave.setText("수정");
                //데이터 가져오기 ..
                //datapicker 설정
                boolean state = false;
                for(int j = 0 ; j <datas.size() ; j++){
                    if(list.get(position).equals(datas.get(j).toString())){
                        deleteposition = j;
                        break;
                    }
                }

                datePicker.updateDate(Integer.parseInt(datas.get(deleteposition).getYear()),
                        Integer.parseInt(datas.get(deleteposition).getMonth())-1
                        ,Integer.parseInt(datas.get(deleteposition).getDate()));
                //
                et.setText(datas.get(deleteposition).getMemo());
                //
                deletelist = position;

            }
        });


   }

    Comparator<String> m_comparator = new Comparator<String>() {
        // 문자열을 비교하는 compare 메소드를 재정의한다.
        public int compare(String lhs, String rhs)
        {
            return lhs.compareTo(rhs);
        }

    };

}
