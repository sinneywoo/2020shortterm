package com.promise.memo.UI;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.promise.memo.Bean.NoteBean;
import com.promise.memo.DB.NoteDao;
import com.promise.memo.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private EditText et_new_title;
    private EditText et_new_content;
    private TextView tv_time;
    private Spinner spinner;
    private NoteDao noteDao;
    private NoteBean note;
    private int myID;
    private String myTitle;
    private String myContent;
    private String myCreate_time;
    private String myUpdate_time;
    private String mySelect_time;
    private String myType;
    private Calendar calendar;
    private String login_user;
    private int flag;//区分是新建还是修改
    private ImageView ivPhoto, ivCanmer;
    private String path = "";
    private ImageView ivEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        getNowTime();

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });

    }

    private void selectTime() {

        calendar = Calendar.getInstance();
        DatePickerDialog dpdialog = new DatePickerDialog(EditActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int month, int day) {
                        // TODO Auto-generated method stub
                        // 更新EditText控件日期 小于10加0
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        final TimePickerDialog tpdialog = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                tv_time.setText(format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        dpdialog.show();
        dpdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                tpdialog.show();
            }
        });
    }

    private void init() {
        et_new_title = (EditText) findViewById(R.id.et_new_title);
        et_new_content = (EditText) findViewById(R.id.et_new_content);
        tv_time = (TextView) findViewById(R.id.tv_remindtime);
        spinner = (Spinner) findViewById(R.id.type_select);
        ivEdit = findViewById(R.id.iv_edit);
        ivPhoto = findViewById(R.id.iv_photo);
        ivCanmer = findViewById(R.id.iv_camera);
        ivCanmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPicker();
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPicker();
            }
        });
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 0);//0新建，1编辑
        login_user = intent.getStringExtra("login_user");

        if (flag == 0) {//0新建
            setTitle("新建笔记");
            myCreate_time = getNowTime();
            myUpdate_time = getNowTime();

        } else if (flag == 1) {//1编辑
            Bundle bundle = intent.getBundleExtra("data");
            note = (NoteBean) bundle.getSerializable("note");
            myID = note.getId();
            myTitle = note.getTitle();
            myContent = note.getContent();
            myCreate_time = note.getCreateTime();
            myUpdate_time = note.getUpdateTime();
            mySelect_time = note.getRemindTime();
            login_user = note.getOwner();
            myType = note.getType();
            path = note.getImage();
            setTitle("编辑笔记");
            for (int i = 0; i < 5; i++) {
                if (spinner.getItemAtPosition(i).toString().equals(myType)) {
                    spinner.setSelection(i);
                }
            }
            et_new_title.setText(note.getTitle());
            et_new_content.setText(note.getContent());
            tv_time.setText(mySelect_time);
            if (!TextUtils.isEmpty(path)){
                Glide.with(this).load(path).into(ivEdit);
            }

        }
    }

    private String getNowTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                    LocalMedia media = selectList.get(0);
                    if (media.isCut()) {
                        path = media.getCutPath();

                    } else {
                        path = media.getPath();
                    }
                    Glide.with(this).load(path).into(ivEdit);
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_save://保存笔记
                saveNoteDate();
                break;
            case R.id.action_new_giveup://放弃保存
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNoteDate() {
        String noteremindTime = tv_time.getText().toString();
        if (noteremindTime.equals("点击设置完成时间")) {
            Toast.makeText(EditActivity.this, "请设置日程事件的完成时间", Toast.LENGTH_SHORT).show();
            return;
        }
        String noteTitle = et_new_title.getText().toString();
        if (noteTitle.length() > 14) {
            Toast.makeText(EditActivity.this, "标题长度应在15字以下", Toast.LENGTH_SHORT).show();
            return;
        } else if (noteTitle.isEmpty()) {
            Toast.makeText(EditActivity.this, "标题内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String noteContent = et_new_content.getText().toString();
        if (noteContent.isEmpty()) {
            Toast.makeText(EditActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String notecreateTime = myCreate_time;
        String noteupdateTime = getNowTime();
        Calendar calendar = Calendar.getInstance();
        int noteID = myID;
        noteDao = new NoteDao(this);
        NoteBean note = new NoteBean();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setCreateTime(notecreateTime);
        note.setUpdateTime(noteupdateTime);
        note.setYear(calendar.get(Calendar.YEAR) + "");
        note.setMonth((calendar.get(Calendar.MONTH) + 1) + "");
        note.setDay(calendar.get(Calendar.DAY_OF_MONTH) + "");
        note.setMark(0);
        note.setImage(path);
        note.setRemindTime(noteremindTime);
        note.setType(spinner.getSelectedItem().toString());
        note.setOwner(login_user);
        if (flag == 0) {//新建笔记
            noteDao.insertNote(note);
        } else if (flag == 1) {//修改笔记
            note.setId(noteID);
            noteDao.updateNote(note);
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initPicker() {
        PictureSelector.create(EditActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(320, 320)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(2, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
