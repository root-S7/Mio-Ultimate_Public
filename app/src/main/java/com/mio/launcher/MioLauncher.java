package com.mio.launcher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import com.google.gson.Gson;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.mio.launcher.adapter.ListGame;
import com.mio.launcher.adapter.ListUser;
import com.mio.mclauncher.mcdownload.AssetsJson;
import com.mio.mclauncher.mcdownload.UrlSource;
import com.wingsofts.guaguale.WaveLoadingView;
import com.xw.repo.BubbleSeekBar;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import cosine.boat.BoatActivity;
import cosine.boat.LauncherConfig;
import cosine.boat.LoadMe;
import cosine.boat.MainActivity;
import cosine.boat.MinecraftVersion;

public class MioLauncher extends AppCompatActivity implements OnClickListener {
    int version = 1;
    //????????????
    String ip = "49.234.85.55";
    SharedPreferences msh;
    SharedPreferences.Editor mshe;
    //?????????
    ConstraintLayout background;
    TextView tip;
    ScrollView tip_container;
    //toolbar??????
    Toolbar main_toolbar;
    Button toolbar_button_home, toolbar_button_exit;
    TextView toolbar_text_state, toolbar_text_info;
    //????????????
    LinearLayout startGame;
    Button startGame_choose;
    TextView versiontext;
    //??????????????????
    Button left_btn_user, left_btn_version, left_btn_gamelist, left_btn_plugin, left_btn_setting, left_btn_about, left_btn_log;
    LinearLayout layout_user, layout_version, layout_gamelist, layout_gamedir, layout_setting, layout_about, layout_log;
    List<LinearLayout> list_layouts;
    //????????????
    LinearLayout layout_user_adduser;
    ListView layout_user_listview;
    ListUser adaptwr_user;
    List<UserBean> userList;
    //??????????????????
    LinearLayout layout_version_modctrl, layout_version_packctrl, layout_version_refresh, layout_version_reset;
    EditText layout_version_editJvm;
    Button layout_version_save;
    //??????????????????
    LinearLayout layout_gamelist_addgame, layout_gamelist_refresh, layout_gamelist_move;
    ListView layout_gamelist_listview;
    ListGame adapter_game;
    //??????????????????
    ImageView layout_plugin_install_installer;
    TextView layout_plugin_text_state_installer;
    LinearLayout layout_plugin_open_installer;
    //????????????
    LinearLayout layout_log_share;
    LinearLayout layout_log_refresh;
    EditText layout_log_edit;
    //?????????????????????
    Button layout_settingButtonMouse, layout_settingButtonBackground, layout_settingButtonFix, layout_settingButtonHelp, layout_settingButtonToCmd, layout_settingButtonChooseGif, layout_settingButtonChangeFbl;
    CheckBox layout_settingCheckBoxFull,layout_settingCheckBoxRender,layout_settingCheckBoxCaci;
    Button layout_settingButtonUpdate;
    ImageView layout_settingImageViewDeleteRuntime;
    //??????
    Button layout_about_donate,layout_about_futureplan;
    TextView layout_about_donationlist;
    //??????
    PopupWindow popupWindow;
    //????????????
    //?????????????????????
    WaveLoadingView wave;
    private boolean flag_first = true;
    private TextView textFile, textProgress, textTotalProgress, textSpeed, textInfo;
    private ProgressBar progressBar, totalProgressBar;
    private AlertDialog mDialog;
    private int fileCount = 0, fileCurrentCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MioUtils.hideBottomMenu(this, true);
        setContentView(R.layout.activity_main);
        //?????????????????????
        msh = getSharedPreferences("Mio", MODE_PRIVATE);
        mshe = msh.edit();
        //?????????UI
        background = findViewById(R.id.activity_mainRelativeLayout);
        if (new File(MioInfo.DIR_MAIN, "bg.png").exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(new File(MioInfo.DIR_MAIN, "bg.png").getAbsolutePath());
            BitmapDrawable bd = new BitmapDrawable(bitmap);
            background.setBackground(bd);
        }
        initUI();
        initOthers();
        initLoadingView();
        if (msh.getBoolean("Ultimate", true)) {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("??????").setMessage("???????????????????????????-Ultimate???????????????????????????????????????????????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://b23.tv/EVcXx9");
                    intent.setData(content_url);
                    Toast.makeText(MioLauncher.this, "???Ultimate??????????????????????????????", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
            }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            mshe.putBoolean("Ultimate", false);
            mshe.commit();
        }
//        new Thread(()->{
//            try {
//                CurseAPIMinecraft.initialize();
////                Log.e("URL",CurseAPI.fileDownloadURL(316059, 2839369).get().toString());
////                CurseAPI.downloadFileToDirectory(316059, 2839369, Paths.get(MioInfo.DIR_TEMP));
////                CurseModpack modpack= CurseModpack.fromJSON(Paths.get("/storage/emulated/0/MioLauncher/.minecraft/temp/manifest.json"));
////                CurseFiles<CurseFile> curseFiles= modpack.files();
////
////                for (CurseFile curseFile:curseFiles){
////                    CurseAPI.downloadFileToDirectory(curseFile.projectID(), curseFile.id(), Paths.get(MioInfo.DIR_TEMP));
////                    Log.e("??????????????????",""+curseFile.id());
////                }
////                Log.e("??????????????????","??????");
//                //mc id 432
////                for(CurseGameVersion version:CurseAPI.gameVersions(432).get()){
////                    Log.e("mc",version.versionString());
////                }
//                for(CurseCategory category:CurseAPI.game(432).get().categories()){
//                    Log.e("mc",category.toString());
//                }
//                Log.e("mc",CurseAPI.game(432).get().toString());
//                CurseAPI.searchProjects()
//            } catch (CurseException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        MioUtils.hideBottomMenu(this, true);
        if (flag_first) {
            flag_first = false;
        }
    }
    private void initOthers() {
        tip = findViewById(R.id.home_text_content);
        tip_container = findViewById(R.id.tip_container);
        if (MioUtils.isNetworkConnected(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String str = "";
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL("http://" + ip + "/Mio/tips.txt").openConnection();
                        con.setConnectTimeout(5000);
                        InputStream in = con.getInputStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        String temp = null;

                        while ((temp = bfr.readLine()) != null) {
                            str += temp + "\n";
                        }
                        bfr.close();
                        in.close();
                        con.disconnect();

                    } catch (IOException e) {
                        str += e.toString();
                    }
                    final String finalStr = str;
                    runOnUiThread(() -> {
                        tip.setText(finalStr);
                    });
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL("http://" + ip + "/Mio/donation.txt").openConnection();
                        con.setConnectTimeout(5000);
                        InputStream in = con.getInputStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        String temp = null;
                        String str = "";
                        while ((temp = bfr.readLine()) != null) {
                            str += temp + "\n";
                        }
                        bfr.close();
                        in.close();
                        con.disconnect();
                        final String result = str;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                layout_about_donationlist.setText(result);
                            }
                        });
                    } catch (final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                layout_about_donationlist.setText("??????:" + e.toString());
                            }
                        });
                    }
                }
            }).start();
        }
    }
    private void initUI() {
        //toolbar??????
        main_toolbar = findViewById(R.id.main_toolbar);
        main_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(main_toolbar);
        toolbar_button_home = findButton(R.id.toolbar_button_home);
        toolbar_button_exit = findButton(R.id.toolbar_button_exit);
        toolbar_text_state = findViewById(R.id.main_text_state);
        toolbar_text_info = findViewById(R.id.main_text_info);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String str = Build.BRAND + " " + Build.MODEL + " Android " + Build.VERSION.RELEASE + " " + MioUtils.getCpuName();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar_text_info.setText(str);
                    }
                });
            }
        }).start();
        //????????????
        startGame = findViewById(R.id.layout_startgame);
        startGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View p1) {
                final File splash = new File(MioInfo.config.get("currentVersion"), "config/splash.properties");
                try {
                    splash.getParentFile().mkdirs();
                    splash.createNewFile();
                }catch (IOException e) {
                }
                try{
                    FileInputStream in = new FileInputStream(splash);
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    in.close();
                    String ss = new String(b);
                    if (!ss.contains("enabled=false")) {
                        AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this).setTitle("??????").setMessage("?????????????????????????????????????????????????????????????????????????").setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                try {
                                    InputStream in = getAssets().open("splash.properties");
                                    byte[] b = new byte[in.available()];
                                    in.read(b);
                                    in.close();
                                    FileOutputStream out = new FileOutputStream(splash);
                                    out.write(b);
                                    out.flush();
                                    out.close();
                                    gameStart();
                                } catch (IOException e) {
                                    toast(e.toString());
                                }
                            }
                        }).setNegativeButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                        gameStart();
                                    }
                        }).create();
                        dialog.show();
                    }else {
                        gameStart();
                    }
                } catch (Exception e) {
                }
            }
        });
        versiontext = findViewById(R.id.activity_main_versiontext);
        startGame_choose = findViewById(R.id.activity_main_choose_version);
        List<String> gamelist = new ArrayList<>();
        String[] versions = new File(MioInfo.DIR_VERSIONS).list();
        if (versions.length != 0) {
            for (String s : versions) {
                gamelist.add(s);
            }
        }
        adapter_game = new ListGame(this, gamelist);
        startGame_choose.setOnClickListener((v) -> {
            ListPopupWindow menu = new ListPopupWindow(this);
            menu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapter_game.getList()));
            menu.setDropDownGravity(Gravity.LEFT);
            menu.setAnchorView(startGame);
            menu.setModal(true);
            menu.setOnItemClickListener((parent, view, position, id) -> {
                versiontext.setText(adapter_game.getList().get(position));
                MioInfo.config.set("currentVersion", new File(MioInfo.DIR_VERSIONS, adapter_game.getList().get(position)).getAbsolutePath());
                menu.dismiss();
            });
            menu.show();
        });
        if (MioInfo.config == null) {
            MioInfo.config = LauncherConfig.fromFile(new File(MioInfo.DIR_MAIN, "MioConfig.json").getAbsolutePath());
        }
        if (!new File(MioInfo.config.get("currentVersion")).exists()) {
            versiontext.setText("???????????????");
        } else {
            versiontext.setText(new File(MioInfo.config.get("currentVersion")).getName());
        }
        //??????????????????
        left_btn_user = findButton(R.id.main_button_user);
        left_btn_version = findButton(R.id.main_button_version);
        left_btn_gamelist = findButton(R.id.main_button_gamelist);
        left_btn_plugin = findButton(R.id.main_button_plugin);
        left_btn_setting = findButton(R.id.main_button_setting);
        left_btn_about = findButton(R.id.main_button_about);
        left_btn_log = findButton(R.id.main_button_log);
        layout_user = findViewById(R.id.layout_user);
        layout_version = findViewById(R.id.layout_version);
        layout_gamelist = findViewById(R.id.layout_gamelist);
        layout_gamedir = findViewById(R.id.layout_gamedir);
        layout_setting = findViewById(R.id.layout_setting);
        layout_about = findViewById(R.id.layout_about);
        layout_log = findViewById(R.id.layout_log);
        list_layouts = new ArrayList<LinearLayout>();
        list_layouts.add(layout_user);
        list_layouts.add(layout_version);
        list_layouts.add(layout_gamelist);
        list_layouts.add(layout_gamedir);
        list_layouts.add(layout_setting);
        list_layouts.add(layout_about);
        list_layouts.add(layout_log);
        for (View layout : list_layouts) {
            layout.setVisibility(View.INVISIBLE);
        }
        //????????????
        layout_user_adduser = findViewById(R.id.layout_user_adduser);
        layout_user_adduser.setOnClickListener(this);
        layout_user_listview = findViewById(R.id.layout_user_listview);
        parseJsonToUser();
        adaptwr_user = new ListUser(this, userList);
        layout_user_listview.setAdapter(adaptwr_user);
        //??????????????????
        layout_version_modctrl = findViewById(R.id.layout_version_modctrl);
        layout_version_modctrl.setOnClickListener(this);
        layout_version_packctrl = findViewById(R.id.layout_version_packctrl);
        layout_version_packctrl.setOnClickListener(this);
        layout_version_editJvm = findViewById(R.id.layout_version_editJvm);
        layout_version_editJvm.setText(MioInfo.config.get("extraJavaFlags"));
        layout_version_save = findViewById(R.id.layout_version_save);
        layout_version_save.setOnClickListener(this);
        layout_version_refresh = findViewById(R.id.layout_version_refresh);
        layout_version_refresh.setOnClickListener(this);
        layout_version_reset = findViewById(R.id.layout_version_reset);
        layout_version_reset.setOnClickListener(this);
        //??????????????????
        layout_gamelist_addgame = findViewById(R.id.layout_gamelist_addgame);
        layout_gamelist_addgame.setOnClickListener(this);
        layout_gamelist_refresh = findViewById(R.id.layout_gamelist_refresh);
        layout_gamelist_refresh.setOnClickListener(this);
        layout_gamelist_move = findViewById(R.id.layout_gamelist_move);
        layout_gamelist_move.setOnClickListener(this);
        layout_gamelist_listview = findViewById(R.id.layout_gamelist_listview);
        layout_gamelist_listview.setAdapter(adapter_game);
        //??????????????????
        layout_plugin_install_installer = findViewById(R.id.layout_plugin_install_installer);
        layout_plugin_install_installer.setOnClickListener(this);
        layout_plugin_text_state_installer = findViewById(R.id.layout_plugin_text_state_installer);
        if (checkApplication("com.mio.mclauncher")) {
            layout_plugin_text_state_installer.setText("?????????");
        }
        layout_plugin_open_installer = findViewById(R.id.layout_plugin_open_installer);
        layout_plugin_open_installer.setOnClickListener(this);
        //????????????
        layout_log_share = findViewById(R.id.layout_log_share);
        layout_log_share.setOnClickListener(this);
        layout_log_refresh = findViewById(R.id.layout_log_refresh);
        layout_log_refresh.setOnClickListener(this);
        layout_log_edit = findViewById(R.id.layout_log_edit);
        new Thread(() -> {
            String s = MioUtils.readTxt(new File(MioInfo.DIR_MAIN, "boat_output.txt").getAbsolutePath());
            runOnUiThread(() -> layout_log_edit.setText(s.contains("??????") ? "?????????" : s));
        }).start();
        //?????????????????????
        layout_settingButtonMouse = findButton(R.id.layout_settingButtonMouse);
        layout_settingButtonBackground = findButton(R.id.layout_settingButtonBackground);
        layout_settingButtonFix = findButton(R.id.layout_settingButtonFix);
        layout_settingButtonHelp = findButton(R.id.layout_settingButtonHelp);
        layout_settingButtonChangeFbl = findButton(R.id.layout_settingButtonChangeFbl);
        layout_settingCheckBoxFull = findViewById(R.id.layout_settingCheckBoxFull);
        layout_settingCheckBoxFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                mshe.putBoolean("??????", p2);
                mshe.commit();
            }
        });
        layout_settingCheckBoxFull.setChecked(msh.getBoolean("??????", false));
        layout_settingCheckBoxRender = findViewById(R.id.layout_settingCheckBoxRender);
        layout_settingCheckBoxRender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {
                if (p2) {
                    mshe.putString("?????????", "gl4es_1.1.5.so");
                    LoadMe.render = "gl4es_1.1.5.so";
                } else {
                    mshe.putString("?????????", "gl4es_1.1.4.so");
                    LoadMe.render = "gl4es_1.1.4.so";
                }
                mshe.commit();
            }
        });
        LoadMe.render = msh.getString("?????????", "gl4es_1.1.4.so");
        layout_settingCheckBoxRender.setChecked(msh.getString("?????????", "gl4es_1.1.4.so").equals("gl4es_1.1.5.so"));
        layout_settingCheckBoxCaci = findViewById(R.id.layout_settingCheckBoxCaci);
        layout_settingCheckBoxCaci.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        new File("/data/data/com.mio.launcher/app_runtime/j2re-image", "8").createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    new File("/data/data/com.mio.launcher/app_runtime/j2re-image", "8").delete();
                }
            }
        });
        layout_settingCheckBoxCaci.setChecked(new File("/data/data/com.mio.launcher/app_runtime/j2re-image", "8").exists());
        layout_settingButtonUpdate = findViewById(R.id.layout_settingButtonUpdate);
        layout_settingButtonUpdate.setOnClickListener(this);
        layout_settingButtonToCmd = findViewById(R.id.layout_settingButtonToCmd);
        layout_settingButtonToCmd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View p1) {
                startActivity(new Intent(MioLauncher.this, MainActivity.class));
            }
        });
        layout_settingButtonChooseGif = findViewById(R.id.layout_settingButtonChooseGif);
        layout_settingButtonChooseGif.setOnClickListener(this);
        layout_settingImageViewDeleteRuntime = findViewById(R.id.layout_settingImageViewDeleteRuntime);

        layout_settingImageViewDeleteRuntime.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View p1) {
                AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this)
                        .setTitle("??????")
                        .setMessage("??????????????????????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                MioUtils.deleteFile(new File(MioLauncher.this.getApplicationContext().getFilesDir().getParent(), "/app_runtime").getAbsolutePath());
                                toast("????????????????????????????????????????????????????????????????????????");
//								startActivity(new Intent(MioLauncher.this, Activity_Download.class));
                                finish();
                            }
                        })
                        .setNegativeButton("??????", null)
                        .setNeutralButton("?????????????????????????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface p1, int p2) {
                                MioUtils.deleteFile(new File(MioLauncher.this.getApplicationContext().getFilesDir().getParent(), "/app_runtime").getAbsolutePath());
                                MioUtils.deleteFile(new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/runtime/mioruntimev4.zip").getAbsolutePath());
                                toast("??????????????????????????????????????????????????????????????????????????????????????????");
//								startActivity(new Intent(MioLauncher.this, Activity_Download.class));
                                finish();
                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });
        //??????
        layout_about_donate = findButton(R.id.layout_about_donate);
        layout_about_futureplan = findButton(R.id.layout_about_futureplan);
        layout_about_donationlist = findViewById(R.id.layout_about_donationlist);
    }
    private void gameStart() {
        if (!versiontext.getText().toString().equals("???????????????")) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("?????????????????????????????????...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            boolean ???????????? = new File(MioInfo.config.get("currentVersion"), "????????????").exists();
            boolean ?????????????????? = new File(MioInfo.config.get("currentVersion"), "??????????????????").exists();
            new Thread(() -> {
                List<String> libs = new ArrayList<>();
                List<String> objects = new ArrayList<>();
                if (!??????????????????) {
                    MinecraftVersion mv = MinecraftVersion.fromDirectory(new File(MioInfo.config.get("currentVersion")));
                    if (mv == null) {
                        runOnUiThread(() -> {
                            toast("????????????????????????????????????????????????????????????");
                        });
                        return;
                    }
                    if (mv.inheritsFrom != null) {
                        if (!new File(MioInfo.DIR_VERSIONS, mv.inheritsFrom).exists()) {
                            runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("??????").setMessage("?????????????????????" + mv.inheritsFrom + "??????????????????(????????????????????????)").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(MioLauncher.this, ActivityDownload.class);
                                        Bundle b = new Bundle();
                                        b.putString("version", mv.inheritsFrom);
                                        i.putExtras(b);//??????Bundle???????????????
                                        startActivity(i);//???????????????activity??????i????????????
                                    }
                                }).setNegativeButton("??????", null).create();
                                dialog.show();
                            });
                            progressDialog.dismiss();
                            return;
                        }
                    }
                    for (String lib : mv.getLibraries()) {
                        if (!????????????) {
                            String sha1 = MioUtils.getFileSha1(new File(MioInfo.DIR_LIBRARIES, lib).getAbsolutePath());
                            if (sha1 != null) {
                                if (!sha1.equals(mv.getSHA1(lib))) {
                                    if (mv.getSHA1(lib) != null) {
                                        new File(MioInfo.DIR_LIBRARIES, lib).delete();
                                    }
                                }
                            }
                        }
                        if (!new File(MioInfo.DIR_LIBRARIES, lib).exists()) {
                            libs.add(lib);
                        }
                    }
                    if (mv.assets != null) {
                        if (new File(MioInfo.DIR_INDEXES, mv.assets + ".json").exists()) {
                            for (String obj : getAssets(new File(MioInfo.DIR_INDEXES, mv.assets + ".json").getAbsolutePath())) {
                                if (new File(MioInfo.DIR_OBJECTS, obj).exists()) {
                                    if (!????????????) {
                                        String sha1 = MioUtils.getFileSha1(new File(MioInfo.DIR_OBJECTS, obj).getAbsolutePath());
                                        if (sha1 != null) {
                                            if (!sha1.equals(new File(MioInfo.DIR_OBJECTS, obj).getName())) {
                                                new File(MioInfo.DIR_OBJECTS, obj).delete();
                                            }
                                        }
                                    }
                                }
                                if (!new File(MioInfo.DIR_OBJECTS, obj).exists()) {
                                    objects.add(obj);
                                }
                            }
                        } else {
                            runOnUiThread(() -> {
                                AlertDialog dialog = new AlertDialog.Builder(this).setTitle("??????").setMessage("?????????????????????" + new File(MioInfo.DIR_INDEXES, mv.assets + ".json").getName() + "??????????????????(????????????????????????)").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(MioLauncher.this, ActivityDownload.class);
                                        Bundle b = new Bundle();
                                        b.putString("version", mv.assets.equals("legacy") ? "1.6.4" : mv.inheritsFrom);
                                        i.putExtras(b);//??????Bundle???????????????
                                        startActivity(i);//???????????????activity??????i????????????
                                    }
                                }).setNegativeButton("??????", null).create();
                                dialog.show();

                            });
                            progressDialog.dismiss();
                            return;
                        }

                    }

                }
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    if (libs.size() != 0) {
                        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("??????").setMessage("?????????????????????????????????????????????????????????????????????").setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                              FileDownloader.setup(MioLauncher.this);
                                initDialog();
                                downloadLibraries(libs);
                            }
                        }).setNegativeButton("???", null).create();
                        dialog.show();
                    } else if (objects.size() != 0) {
                        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("??????").setMessage("????????????????????????????????????????????????????????????????????????").setPositiveButton("???", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                              FileDownloader.setup(MioLauncher.this);
                                initDialog();
                                downloadObjects(objects);
                            }
                        }).setNegativeButton("???", null).create();
                        dialog.show();
                    }else {
                        if (adaptwr_user.getSelected() == null) {
                            toast("???????????????");
                            return;
                        }
                        if (adaptwr_user.getSelected().getUserState().equals("????????????")) {
                            View temlView = LayoutInflater.from(MioLauncher.this).inflate(R.layout.view_progress, null);
                            final AlertDialog tempdialog = new AlertDialog.Builder(MioLauncher.this).setTitle("?????????......").setView(temlView).create();
                            tempdialog.setCancelable(false);
                            tempdialog.setCanceledOnTouchOutside(false);
                            final MioLogin tempMioLogin = new MioLogin();
                            tempMioLogin.setUrl(adaptwr_user.getSelected().getUrl().equals("") ? "https://authserver.mojang.com" : adaptwr_user.getSelected().getUrl());
                            tempMioLogin.setListener(new MioLogin.LoginListener() {
                                @Override
                                public void onStart() {
                                    tempdialog.show();
                                }

                                @Override
                                public void onSucceed(ArrayMap<String, String> map) {
                                    MioInfo.config.set("auth_access_token", map.get("accessToken"));
                                    MioInfo.config.set("auth_uuid", map.get("uuid"));
                                    try {
                                        JSONObject json = new JSONObject(msh.getString("users", ""));
                                        UserBean bean = adaptwr_user.getSelected();
                                        JSONObject json2 = json.getJSONObject(bean.getUserName());
                                        json2.put("token", map.get("accessToken"));
                                        json.put(bean.getUserName(), json2);
                                        mshe.putString("users", json.toString());
                                        mshe.commit();
                                    } catch (JSONException e) {

                                    }
                                    adaptwr_user.notifyDataSetChanged();
                                    tempdialog.dismiss();
                                    startActivity(new Intent(MioLauncher.this, BoatActivity.class));
                                }
                                @Override
                                public void onError(String error) {
                                    tempdialog.dismiss();
                                    AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this).setTitle("??????").setMessage("???????????????" + error + "\n?????????????????????").setPositiveButton("???", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dia, int which) {
                                            tempMioLogin.login(adaptwr_user.getSelected().getUserAccount(), adaptwr_user.getSelected().getUserPass());
                                        }
                                    }).setNegativeButton("???", null).create();
                                    dialog.show();
                                }
                            });
                            tempMioLogin.checkOrRefresh(adaptwr_user.getSelected().getToken());
                        }
                        Intent intent = new Intent(MioLauncher.this, BoatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        startActivity(intent);
                    }
                });
            }).start();
        } else {
            toast("?????????????????????");
        }
    }
    private void initDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.alert_download_progress, null);
        textFile = v.findViewById(R.id.alert_downloadTextViewFile);
        textProgress = v.findViewById(R.id.alert_downloadTextViewProgress);
        progressBar = v.findViewById(R.id.alert_downloadProgressBar);
        textTotalProgress = v.findViewById(R.id.alert_downloadTextViewTotalProgress);
        textSpeed = v.findViewById(R.id.alert_downloadTextViewSpeed);
        totalProgressBar = v.findViewById(R.id.alert_downloadProgressBarTotalProgress);
        textInfo = v.findViewById(R.id.alert_download_info);
        mDialog = new AlertDialog.Builder(this).setView(v).setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface p1, int p2) {
                        mDialog.dismiss();
                    }
        }).create();
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                FileDownloader.getImpl().pauseAll();
                FileDownloader.getImpl().clearAllTaskData();
            }
        });
    }
    public List<String> getAssets(String filePath) {
        AssetsJson mAssetsJson;
        List<String> assets = new ArrayList<>();
        try {
            FileReader reader = new FileReader(filePath);
            mAssetsJson = new Gson().fromJson(reader, AssetsJson.class);
            reader.close();
        }catch (Exception e) {
            return null;
        }
        HashMap<String, AssetsJson.MinecraftAssetInfo> objects = mAssetsJson.getObjects();
        Set<String> keySet = objects.keySet();
        for (String key : keySet) {
            AssetsJson.MinecraftAssetInfo info = objects.get(key);
            String path = info.getHash().substring(0, 2);
            String url = path + "/" + info.getHash();
            assets.add(url);
        }
        return assets;
    }
    private void downloadLibraries(List<String> libs) {
        textInfo.setText("?????????????????????");
        fileCurrentCount = 0;
        FileDownloadListener queueTarget = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {}
            @Override
            protected void started(BaseDownloadTask task) {
                textFile.setText(task.getFilename());
            }
            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {}
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                long progress = soFarBytes * 100 / totalBytes;
                progressBar.setProgress((int) progress);
                textProgress.setText(progress + "%");
                textSpeed.setText(task.getSpeed() + "kb/s");
            }
            @Override
            protected void blockComplete(BaseDownloadTask task) {}
            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                //toast("??????");
            }
            @Override
            protected void completed(BaseDownloadTask task) {
                fileCurrentCount++;
                totalProgressBar.setProgress(fileCurrentCount);
                textTotalProgress.setText(fileCurrentCount * 100 / fileCount + "%");
                if (fileCurrentCount == totalProgressBar.getMax()) {
                    mDialog.dismiss();
                    Toast.makeText(MioLauncher.this, "???????????????", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //toast("??????");
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                toast("?????????" + e.toString());
            }
            @Override
            protected void warn(BaseDownloadTask task) {
                //toast("??????");
            }
        };
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (String lib : libs) {
            tasks.add(FileDownloader.getImpl().create(UrlSource.MCBBS_LIBRARIES + "/" + lib).setTag(lib).setPath(new File(MioInfo.DIR_LIBRARIES, lib).getAbsolutePath()).setAutoRetryTimes(5));
        }
        fileCount = libs.size();
        totalProgressBar.setMax(fileCount);
        queueSet.setAutoRetryTimes(5);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
    private void downloadObjects(List<String> objects) {
        textInfo.setText("??????????????????????????????");
        fileCurrentCount = 0;
        FileDownloadListener queueTarget = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {}
            @Override
            protected void started(BaseDownloadTask task) {
                textFile.setText(task.getFilename());
            }
            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {}
            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                long progress = soFarBytes * 100 / totalBytes;
                progressBar.setProgress((int) progress);
                textProgress.setText(progress + "%");
                textSpeed.setText(task.getSpeed() + "kb/s");
            }
            @Override
            protected void blockComplete(BaseDownloadTask task) {
            }
            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                //toast("??????");
            }
            @Override
            protected void completed(BaseDownloadTask task) {
                fileCurrentCount++;
                totalProgressBar.setProgress(fileCurrentCount);
                textTotalProgress.setText(fileCurrentCount * 100 / fileCount + "%");
                if (fileCurrentCount == totalProgressBar.getMax()) {
                    mDialog.dismiss();
                    Toast.makeText(MioLauncher.this, "???????????????", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //toast("??????");
            }
            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                toast("?????????" + e.toString());
            }
            @Override
            protected void warn(BaseDownloadTask task) {
                //toast("??????");
            }
        };
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(queueTarget);
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (String obj : objects) {
            tasks.add(FileDownloader.getImpl().create(UrlSource.MCBBS_ASSETS_OBJS + "/" + obj).setTag(obj).setPath(new File(MioInfo.DIR_OBJECTS, obj).getAbsolutePath()).setAutoRetryTimes(5));
        }
        fileCount = objects.size();
        totalProgressBar.setMax(fileCount);
        queueSet.setAutoRetryTimes(5);
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
    //??????????????????
    private void parseJsonToUser() {
        if (userList == null) {
            userList = new ArrayList<UserBean>();
        } else {
            userList.clear();
        }
        if ((!msh.getString("users", "").equals("")) && !msh.getString("users", "").equals("{}")) {
            try {
                JSONObject json = new JSONObject(msh.getString("users", ""));
                JSONArray jsa = json.names();
                for (int i = 0; i < jsa.length(); i++) {
                    UserBean user = new UserBean();
                    user.setUserName(jsa.getString(i));
                    JSONObject json2 = json.getJSONObject(jsa.getString(i));
                    user.setUserState(json2.getString("state"));
                    user.setIsSelected(json2.getBoolean("isSelected"));
                    user.setUuid(json2.getString("uuid"));
                    user.setToken(json2.getString("token"));
                    user.setUrl(json2.getString("url"));
                    JSONArray jsa2 = json2.getJSONArray("loginInfo");
                    user.setUserAccount(jsa2.getString(0));
                    user.setUserPass(jsa2.getString(1));
                    userList.add(user);
                }
            } catch (JSONException e) {
                toast(e.toString());
            }
        }


    }

    private void initLoadingView() {
        View base = LayoutInflater.from(this).inflate(R.layout.waveloading, null);
        wave = base.findViewById(R.id.wave);
        ImageButton exit = base.findViewById(R.id.waveloadingImageButtonExit);
        exit.setVisibility(View.INVISIBLE);
        popupWindow = new PopupWindow();
        popupWindow.setWidth(LayoutParams.FILL_PARENT);
        popupWindow.setHeight(LayoutParams.FILL_PARENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(base);

    }

    //find???????????????????????????
    private Button findButton(int id) {
        Button temp = findViewById(id);
        temp.setOnClickListener(this);
        return temp;
    }
    @Override
    public void onClick(View v) {
        if (v == toolbar_button_home) {
            toolbar_text_state.setText("??????");
            leftBtnKeep(null);
            startGame.setVisibility(View.VISIBLE);
            tip_container.setVisibility(View.VISIBLE);
            AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
            disappearAnimation.setDuration(500);
            for (final View vv : list_layouts) {
                if (vv.getVisibility() == View.VISIBLE) {
                    vv.startAnimation(disappearAnimation);
                    disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation p1) {
                        }

                        @Override
                        public void onAnimationEnd(Animation p1) {
                            vv.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation p1) {
                        }
                    });
                }
            }
        } else if (v == toolbar_button_exit) {
            finish();
            System.exit(0);
        } else if (v == left_btn_user) {
            showTargetView(layout_user);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_version) {
            showTargetView(layout_version);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_gamelist) {
            showTargetView(layout_gamelist);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_plugin) {
            showTargetView(layout_gamedir);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_setting) {
            showTargetView(layout_setting);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_log) {
            showTargetView(layout_log);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == left_btn_about) {
            showTargetView(layout_about);
            leftBtnKeep(v);
            toolbar_text_state.setText(((Button) v).getText().toString());
        } else if (v == layout_user_adduser) {
//            if(Splash.specialMode&&Splash.serverMode){
//                toast("??????????????????????????????????????????????????????");
//                return;
//            }
            View tempView = LayoutInflater.from(MioLauncher.this).inflate(R.layout.alert_login, null);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("????????????")
                    .setView(tempView)
                    .create();
            dialog.show();
            final EditText tempUser = tempView.findViewById(R.id.alert_login_edit_account);
            final EditText tempPass = tempView.findViewById(R.id.alert_login_edit_password);
            Button tempLogin = tempView.findViewById(R.id.alert_login_btn_login);
            Button tempCancle = tempView.findViewById(R.id.alert_login_btn_cancle);
            CheckBox tempCheck = tempView.findViewById(R.id.alert_login_check);
            final LinearLayout tempPassLinear = tempView.findViewById(R.id.alert_login_linear_pass);
            EditText serverUrl = tempView.findViewById(R.id.alert_login_edit_url);

            tempLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    final String temp1 = tempUser.getText().toString();
                    final String temp2 = tempPass.getText().toString();
                    final String temp3 = serverUrl.getText().toString();
                    if (tempCheck.isChecked()) {
                        if (temp1.equals("")) {
                            toast("?????????????????????");
                        } else {
                            addUserToJson(temp1, "????????????", false, "", "", "", "");
                            adaptwr_user.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    } else {
                        if (temp1.trim().equals("") || temp2.trim().equals("")) {
                            toast("??????????????????????????????");
                        } else {
                            View temlView = LayoutInflater.from(MioLauncher.this).inflate(R.layout.view_progress, null);
                            final AlertDialog tempdialog = new AlertDialog.Builder(MioLauncher.this)
                                    .setTitle("?????????......")
                                    .setView(temlView)
                                    .create();
                            tempdialog.setCancelable(false);
                            tempdialog.setCanceledOnTouchOutside(false);

                            MioLogin tempMioLogin = new MioLogin();
                            if (!serverUrl.getText().toString().trim().equals("")) {
                                tempMioLogin.setUrl(serverUrl.getText().toString());
                            }
                            tempMioLogin.setListener(new MioLogin.LoginListener() {
                                @Override
                                public void onStart() {
                                    tempdialog.show();
                                }

                                @Override
                                public void onSucceed(ArrayMap<String, String> map) {
                                    addUserToJson(map.get("name"), "????????????", false, temp1, temp2, temp3, map.get("uuid"), map.get("accessToken"));
                                    adaptwr_user.notifyDataSetChanged();
                                    tempdialog.dismiss();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onError(String error) {
                                    tempdialog.dismiss();
                                    toast("???????????????" + error);
                                }
                            });
                            tempMioLogin.login(temp1, temp2);

                        }
                    }
                }
            });
            tempCancle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    dialog.dismiss();
                }
            });
            tempCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    if (p2) {
                        tempPassLinear.setVisibility(View.GONE);
                    } else {
                        tempPassLinear.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else if (v == layout_version_modctrl) {
//            startActivity(new Intent(MioLauncher.this, MioMod.class));
            Toast.makeText(this, "??????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        } else if (v == layout_version_packctrl) {
//            startActivity(new Intent(MioLauncher.this, MioPack.class));
            Toast.makeText(this, "??????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
        } else if (v == layout_version_save) {
            MioInfo.config.set("extraJavaFlags", layout_version_editJvm.getText().toString());
            toast("?????????");
        } else if (v == layout_version_refresh) {
            layout_version_editJvm.setText(MioInfo.config.get("extraJavaFlags"));
            toast("?????????");
        } else if (v == layout_version_reset) {
            MioInfo.config.set("extraJavaFlags", "-Xmx1024M");
            layout_version_editJvm.setText(MioInfo.config.get("extraJavaFlags"));
            toast("?????????");
        } else if (v == layout_gamelist_addgame) {
            startActivity(new Intent(MioLauncher.this, ActivityDownload.class));
        } else if (v == layout_gamelist_refresh) {
            adapter_game.getList().clear();
            String[] versions = new File(MioInfo.DIR_VERSIONS).list();
            if (versions.length != 0) {
                for (String s : versions) {
                    adapter_game.getList().add(s);
                    adapter_game.notifyDataSetChanged();
                }
            }
        } else if (v == layout_gamelist_move) {
            new LFilePicker()
                    .withActivity(MioLauncher.this)
                    .withRequestCode(6666)
                    .withStartPath("/storage/emulated/0/")
                    .withFileFilter(new String[]{".zip"})
                    .start();
        } else if (v == layout_plugin_install_installer) {
            if (layout_plugin_text_state_installer.getText().equals("?????????")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://wwi.lanzoui.com/iIoUZr7q0di");
                intent.setData(content_url);
                startActivity(intent);
            } else {
                Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
            }

        } else if (v == layout_plugin_open_installer) {
            if (layout_plugin_text_state_installer.getText().equals("?????????")) {
                Toast.makeText(this, "?????????", Toast.LENGTH_SHORT).show();
                return;
            }
            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(MioLauncher.this)
                    .setMessage("???????????????-Installer?????????????????????????????????????????????")
                    .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new LFilePicker().withActivity(MioLauncher.this).withRequestCode(666).withStartPath("/storage/emulated/0/").withFileFilter(new String[]{".jar"}).start();
                        }
                    })
                    .setNegativeButton("??????", null)
                    .create();
            dialog.show();
        } else if (v == layout_log_share) {
            File file = new File(MioInfo.DIR_MAIN, "boat_output.txt");
            if (null != file && file.exists()) {
                Intent share = new Intent(Intent.ACTION_SEND);
                //android7?????????FileProvider
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
                    share.putExtra(Intent.EXTRA_STREAM, contentUri);
                } else {
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                }
                share.setType("*/*");
                share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "????????????"));
            } else {
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
            }

        } else if (v == layout_log_refresh) {
            new Thread(() -> {
                String s = MioUtils.readTxt(new File(MioInfo.DIR_MAIN, "boat_output.txt").getAbsolutePath());
                runOnUiThread(() -> layout_log_edit.setText(s.contains("??????") ? "?????????" : s));
            }).start();
        } else if (v == layout_settingButtonMouse) {
            String[] items = {"??????", "??????"};
            AlertDialog dialog = new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    if (which == 0) {
                        new File(MioUtils.getExternalFilesDir(MioLauncher.this), "???/cursor.png").delete();
                    } else if (which == 1) {
                        Intent intent = new Intent(Intent.ACTION_PICK);  //????????? ACTION_IMAGE_CAPTURE
                        intent.setType("image/*");
                        startActivityForResult(intent, 100);
                    }
                }
            }).setNegativeButton("??????", null).create();
            dialog.show();
        } else if (v == layout_settingButtonBackground) {
            String[] items = {"??????", "??????"};
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            if (which == 0) {
                                background.setBackgroundResource(R.drawable.background);
                                new File(MioUtils.getExternalFilesDir(MioLauncher.this), "???/bg.png").delete();
                            } else if (which == 1) {
                                Intent intent = new Intent(Intent.ACTION_PICK);  //????????? ACTION_IMAGE_CAPTURE
                                intent.setType("image/*");
                                startActivityForResult(intent, 101);
                            }
                        }
                    })
                    .setNegativeButton("??????", null)
                    .create();
            dialog.show();
        } else if (v == layout_settingButtonFix) {
            final String[] items = {"GBK", "GB2312", "GB18030", "UTF-8"};
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("???????????????????????????")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dia, int which) {
                            MioInfo.config.set("extraJavaFlags", MioInfo.config.get("extraJavaFlags") + " -Dfile.encoding=" + items[which]);
                            layout_version_editJvm.append(" -Dfile.encoding=" + items[which]);
                            Toast.makeText(MioLauncher.this, "???????????????:" + items[which], Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("??????", null)
                    .create();
            dialog.show();
        } else if (v == layout_settingButtonUpdate) {
            final Handler up = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this)
                                .setTitle("??????")
                                .setMessage((String) msg.obj)
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dia, int which) {

                                    }
                                })
                                .setNegativeButton("??????", null)
                                .create();
                        dialog.show();
                    } else if (msg.what == 1) {
                        if (Integer.valueOf((String) msg.obj) > version) {
                            if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this), "/???/update").exists()) {
                                new File(MioUtils.getExternalFilesDir(MioLauncher.this), "/???/update").mkdirs();
                            }
                            AlertDialog dialog_up = new AlertDialog.Builder(MioLauncher.this)
                                    .setTitle("??????")
                                    .setMessage("????????????????????????????????????")
                                    .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dia, int which) {
                                            View vi = LayoutInflater.from(MioLauncher.this).inflate(R.layout.alert_update, null);
                                            TextView progressText = vi.findViewById(R.id.alertupdateTextView);
                                            AlertDialog dialog = new AlertDialog.Builder(MioLauncher.this)
                                                    .setTitle("??????")
                                                    .setView(vi)
                                                    .create();
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();

                                        }
                                    }).setNegativeButton("??????", null)
                                    .create();
                            dialog_up.show();

                        } else {
                            Toast.makeText(MioLauncher.this, "???????????????????????????", Toast.LENGTH_LONG).show();
                        }

                    }

                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection con = (HttpURLConnection) new URL("http://" + ip + "/Mio/mio_version.txt").openConnection();
                        InputStream in = con.getInputStream();
                        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                        String temp = null;
                        String str = "";
                        while ((temp = bfr.readLine()) != null) {
                            str += temp;
                        }
                        bfr.close();
                        in.close();
                        con.disconnect();
                        Message msg = new Message();
                        msg.obj = str;
                        msg.what = 1;
                        up.sendMessage(msg);
                    } catch (IOException e) {
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = "??????????????????:" + e.toString();
                        up.sendMessage(msg);
                    }
                }
            }).start();
        } else if (v == layout_settingButtonChooseGif) {
            String[] items = {"??????", "??????"};
            AlertDialog dialog = new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    if (which == 0) {
                        background.setBackgroundResource(R.drawable.background);
                        new File(MioUtils.getExternalFilesDir(MioLauncher.this), "???/mygif.gif").delete();
                    } else if (which == 1) {
                        new LFilePicker().withActivity(MioLauncher.this).withRequestCode(102).withStartPath(MioUtils.getStoragePath()).withFileFilter(new String[]{".gif"}).start();
                    }
                }
            }).setNegativeButton("??????", null).create();
            dialog.show();
        } else if (v == layout_settingButtonHelp) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://b23.tv/EVcXx9");
            intent.setData(content_url);
            Toast.makeText(MioLauncher.this, "???????????????(????????????)", Toast.LENGTH_LONG).show();
            startActivity(intent);
        } else if (v == layout_settingButtonChangeFbl) {
            final View view = LayoutInflater.from(this).inflate(R.layout.alert_change_screen_size, null);
            BubbleSeekBar seekBar = view.findViewById(R.id.alert_change_screen_size_seekBar);
            seekBar.setProgress((int) (msh.getFloat("???????????????", 1.0f) * 100));
            seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                }

                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                }

                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

                }
            });
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setTitle("??????????????????")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mshe.putFloat("???????????????", ((float) seekBar.getProgress()) / 100);
                            mshe.commit();
                        }
                    })
                    .setNegativeButton("??????", null)
                    .create();
            dialog.show();
        } else if (v == layout_about_donate) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("https://afdian.net/@boatmio");
            intent.setData(content_url);
            Toast.makeText(MioLauncher.this, "????????????????????????...", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

    }

    private void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //?????????????????????7.0??????
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "cosine.launcher.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);

    }

    //????????????
    private void addUserToJson(String name, String state, boolean isSelected, String account, String pass, String uuid, String token) {
        try {
            JSONObject json = null;
            if (msh.getString("users", "").equals("")) {
                json = new JSONObject();
            } else {
                json = new JSONObject(msh.getString("users", ""));
            }
            JSONObject userData = new JSONObject();
            userData.put("state", state);
            userData.put("isSelected", isSelected);
            userData.put("loginInfo", new JSONArray().put(0, account).put(1, pass));
            userData.put("uuid", uuid);
            userData.put("token", token);
            userData.put("url", "");
            json.put(name, userData);
            mshe.putString("users", json.toString());
            mshe.commit();
            parseJsonToUser();
        } catch (JSONException e) {
            toast(e.toString());
        }

    }

    private void addUserToJson(String name, String state, boolean isSelected, String account, String pass, String url, String uuid, String token) {
        try {
            JSONObject json = null;
            if (msh.getString("users", "").equals("")) {
                json = new JSONObject();
            } else {
                json = new JSONObject(msh.getString("users", ""));
            }
            JSONObject userData = new JSONObject();
            userData.put("state", state);
            userData.put("isSelected", isSelected);
            userData.put("loginInfo", new JSONArray().put(0, account).put(1, pass));
            userData.put("uuid", uuid);
            userData.put("token", token);
            userData.put("url", url);
            json.put(name, userData);
            mshe.putString("users", json.toString());
            mshe.commit();
            parseJsonToUser();
        } catch (JSONException e) {
            toast(e.toString());
        }

    }

    //??????/????????????
    private void showTargetView(View v) {
        startGame.setVisibility(View.INVISIBLE);
        tip_container.setVisibility(View.INVISIBLE);
        AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);
        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(500);
        if (v.getVisibility() == View.INVISIBLE) {
            v.startAnimation(appearAnimation);
            v.setVisibility(View.VISIBLE);
        }
        for (View vv : list_layouts) {
            if (vv != v && vv.getVisibility() == View.VISIBLE) {
                vv.startAnimation(disappearAnimation);
                vv.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void leftBtnKeep(View b) {
        left_btn_user.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_version.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_gamelist.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_plugin.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_setting.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_about.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_log.setBackgroundResource(R.drawable.layout_button_background);
        left_btn_plugin.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_about.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_gamelist.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_setting.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_user.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_version.setTextColor(Color.parseColor("#FFF97297"));
        left_btn_log.setTextColor(Color.parseColor("#FFF97297"));
        if (b != null) {
            b.setBackgroundResource(R.drawable.layout_button_background_pressed);
            ((Button) b).setTextColor(Color.parseColor("#FFFFFFFF"));
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            Uri uri = data.getData();
            if (requestCode == 100) {
                if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/").exists()) {
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/").mkdirs();
                }
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    FileOutputStream fw = new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/cursor.png");
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 101) {
                if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/").exists()) {
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/").mkdirs();
                }
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    FileOutputStream fw = new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/bg.png");
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(MioUtils.getExternalFilesDir(this) + "/???/bg.png");
                    BitmapDrawable bd = new BitmapDrawable(bitmap);
                    background.setBackground(bd);
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 102) {
                if (!new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/").exists()) {
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/").mkdirs();
                }
                try {
                    final File path = new File((String) data.getStringArrayListExtra("paths").get(0));
                    InputStream in = new FileInputStream(path);
                    FileOutputStream fw = new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this) + "/???/mygif.gif");
                    byte[] b = new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == 233) {
            } else if (requestCode == 666) {
                ComponentName componentName = new ComponentName(
                        "com.mio.mclauncher",   //???????????????App?????????
                        "com.mio.mclauncher.MioActivity");
                //???????????????App??????Activity?????????
                // ComponentName : ????????????
                //???????????????????????????????????????????????????????????????Manifest?????????????????????
                //????????????????????????????????????
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setComponent(componentName);
                bundle.putString("modFile", (String) data.getStringArrayListExtra("paths").get(0));
                intent.putExtra("modFile", bundle);
                startActivity(intent);
            } else if (requestCode == 6666) {
                String path = (String) data.getStringArrayListExtra("paths").get(0);
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("??????????????????????????????...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
                new Thread(() -> {
                    File dir = new File(MioInfo.DIR_TEMP, "??????");
                    try {
                        FileUtils.forceDelete(dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        new ZipFile(path).extractAll(dir.getAbsolutePath());
                    } catch (ZipException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            toast("???????????????" + e.toString());
                        });
                    }
                    File gamedir;
                    if (new File(dir, "boat").exists()) {
                        gamedir = new File(dir, "boat/gamedir");
                    } else if (new File(dir, "gamedir").exists()) {
                        gamedir = new File(dir, "gamedir");
                    } else {
                        runOnUiThread(() -> {
                            toast("???????????????boat?????????????????????");
                            dialog.dismiss();
                        });
                        return;
                    }
                    runOnUiThread(() -> {
                        String[] items = new File(gamedir, "versions").list();
                        AlertDialog dialog1 = new AlertDialog.Builder(MioLauncher.this).setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                new Thread(() -> {
                                    try {
//                                      FileUtils.moveDirectoryToDirectory(new File(gamedir,"assets"),new File(MioInfo.DIR_GAME),true);
//                                      FileUtils.moveDirectoryToDirectory(new File(gamedir,"libraries"),new File(MioInfo.DIR_GAME),true);
//                                      FileUtils.moveDirectoryToDirectory(new File(gamedir,"versions"),new File(MioInfo.DIR_GAME),true);
//                                      FileUtils.moveDirectory(gamedir,new File(MioInfo.DIR_VERSIONS,items[which]));
                                        MioShell.doShell("cp -r -f " + new File(gamedir, "assets").getAbsolutePath() + " " + new File(MioInfo.DIR_GAME).getAbsolutePath());
                                        MioShell.doShell("cp -r -f " + new File(gamedir, "libraries").getAbsolutePath() + " " + new File(MioInfo.DIR_GAME).getAbsolutePath());
                                        MioShell.doShell("cp -r -f " + new File(gamedir, "versions").getAbsolutePath() + " " + new File(MioInfo.DIR_GAME).getAbsolutePath());
                                        for (File s:gamedir.listFiles()){
                                            MioShell.doShell("cp -r -f " + s.getAbsolutePath() + " " + new File(MioInfo.DIR_VERSIONS, items[which]).getAbsolutePath());
                                        }
//                                          MioShell.doShell("cp -r -f " + gamedir.getAbsolutePath() + "/* " + new File(MioInfo.DIR_VERSIONS, items[which]).getAbsolutePath());
                                        MioShell.doShell("rm -rf "+gamedir.getAbsolutePath());
                                        runOnUiThread(() -> {
                                            toast("???????????????????????????");
                                            dialog.dismiss();
                                        });
                                    } catch (Exception e) {
                                        Log.e("???????????????????????????", e.toString());
                                        runOnUiThread(() -> {
                                            toast("???????????????????????????" + e.toString());
                                            dialog.dismiss();
                                        });
                                    }
                                }).start();
                            }
                        }).setTitle("???????????????????????????????????????").setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                dialog.dismiss();
                            }
                        }).create();dialog1.show();
                    });
                }).start();
            }
        }
    }

    public boolean checkApplication(String packageName) {

        if (packageName == null || "".equals(packageName)) {

            return false;

        }

        try {

            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();
        FileDownloader.getImpl().clearAllTaskData();
    }
}
