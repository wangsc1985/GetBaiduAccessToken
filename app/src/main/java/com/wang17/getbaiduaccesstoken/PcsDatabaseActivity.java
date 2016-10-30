package com.wang17.getbaiduaccesstoken;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baidu.solution.pcs.sd.PcsSd;
import com.baidu.solution.pcs.sd.impl.records.UpdateRecord;
import com.baidu.solution.pcs.sd.impl.tables.CreateTable;
import com.baidu.solution.pcs.sd.model.ColumnType;
import com.baidu.solution.pcs.sd.model.Order;
import com.baidu.solution.pcs.sd.model.Record;
import com.baidu.solution.pcs.sd.model.RecordSet;
import com.baidu.solution.pcs.sd.model.Table;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PcsDatabaseActivity extends AppCompatActivity {

    private static final String FAVORITE_TABLE = "AppInfo";
    private static final String ARTIST_INDEX = "artist_index";
    private static final String LANGUAGE_INDEX = "language_index";

    private Button button_create, button_insert, button_update, button_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcs_database);

        button_create = (Button) findViewById(R.id.button_create);
        button_insert = (Button) findViewById(R.id.button_insert);
        button_select = (Button) findViewById(R.id.button_select);
        button_update = (Button) findViewById(R.id.button_update);

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Table table = createFavoriteSongTable(MainActivity.ACCESS_TOKEN);
                    }
                }).start();
            }
        });
        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RecordSet records = insertRecords(MainActivity.ACCESS_TOKEN);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            records = selectRecordsBySongNameAndAlbums(MainActivity.accessToken, "Super Star", "SHE");
            }
        });
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Record records = correctRecords(MainActivity.accessToken, records);
            }
        });
    }

    private Table createFavoriteSongTable(String accessToken) {
        try {
            PcsSd service = new PcsSd(accessToken);
            CreateTable create = service.tables().create(FAVORITE_TABLE);
            create.addColumn("name", "name of song", ColumnType.STRING, true);
            create.addColumn("artist", "artist of song", ColumnType.STRING, true);
            create.addColumn("ablums", "ablums name to which the song belong", ColumnType.STRING, true);
            create.addColumn("createTime", "time of song added to the favorite list", ColumnType.INT, true);
            create.addColumn("language", "song language", ColumnType.STRING, true);
            create.addIndex(ARTIST_INDEX, "artist", Order.ASC).addIndex(LANGUAGE_INDEX, "language", Order.ASC);
            return create.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private RecordSet insertRecords(String accessToken) throws IOException {
        PcsSd service = new PcsSd(accessToken);
        List<Song> favorSongs = new LinkedList<Song>();
        favorSongs.add(new Song("Black Humor", "Jay", "Jay", System.currentTimeMillis(), "Chinese"));
        favorSongs.add(new Song("Super Star", "SHE", "SHE", System.currentTimeMillis(), "Chinese"));
        return service.records().insert(FAVORITE_TABLE, favorSongs).execute();
    }

//    private RecordSet selectRecordsBySongNameAndAlbums(String userAccessToken, String name, String albums) throws IOException {
//        PcsSd service = new PcsSd(MainActivity.accessToken);
//        return service.records().select(FAVORITE_TABLE).addEqualCond("albums", albums).addEqualCond("name", name).execute();
//    }


    private RecordSet correctRecords(String userAccessToken, RecordSet records) throws IOException {
        if (records.getRecords().size() < 1) {
            return new RecordSet();
        }
        UpdateRecord updateRecord = null;
        for (Record wrongRecord : records.getRecords()) {
            Song wrongSong = wrongRecord.toType(Song.class);
            Song correctSong = wrongSong.setAlbums("Super Star");
            if (null == updateRecord) {
                updateRecord = new PcsSd(userAccessToken).records().update(FAVORITE_TABLE, wrongRecord.getKey(), wrongRecord.getModifyTime(), correctSong);
                continue;
            }
            updateRecord.add(wrongRecord.getKey(), wrongRecord.getModifyTime(), wrongRecord);
        }
        return updateRecord.setReplace().execute();
    }
}
