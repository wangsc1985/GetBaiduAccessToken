package com.wang17.getbaiduaccesstoken;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoDBActivity extends AppCompatActivity {

    private static final String _address = "ds053126.mlab.com";
    private static final int _port = 53126;

    private static final String _user = "wangsc";
    private static final String _password = "351489";
    private static final String _database = "app-manager";
    private static final String _collection = "app-info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mongo_db);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        opration();
//            }
//        }).start();
    }

    private void opration() {
        try {
            // Create seed data

            final BasicDBObject[] seedData = createSeedData();

            // Standard URI format: mongodb://[dbuser:dbpassword@]host:port/dbname
            MongoClientURI uri = new MongoClientURI("mongodb://" + _user + ":" + _password + "@ds053126.mlab.com:53126/app-manager");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());
        /*
         * First we'll add a few app_info. Nothing is required to create the
         * app_info collection; it is created automatically when we insert.
         */

            DBCollection app_info = db.getCollection("app-info");

            // Note that the insert method can take either an array or a document.

//            app_info.insert(seedData);

        /*
         * Then we need to give Boyz II Men credit for their contribution to
         * the hit "One Sweet Day".
         */

//            BasicDBObject updateQuery = new BasicDBObject("song", "爱你一万年");
//            app_info.update(updateQuery, new BasicDBObject("$set", new BasicDBObject("artist", "王世超")));

        /*
         * Finally we run a query which returns all the hits that spent 10
         * or more weeks at number 1.
         */

            BasicDBObject findQuery = new BasicDBObject("AppName", "RC");
            BasicDBObject orderBy = new BasicDBObject("decade", 1);

            DBCursor docs = app_info.find();
//                    .find(findQuery).sort(orderBy);

            while (docs.hasNext()) {
                DBObject doc = docs.next();
                doc.get("VersionCode");

            }

            // Since this is an example, we'll clean up after ourselves.

            app_info.drop();

            // Only close the connection when your app is terminating

            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    {
//        "_id": 1,
//            "AppName": "RC",
//            "VersionCode": 14,
//            "VersionName": "3.0.0"
//    }

    private BasicDBObject[] createSeedData() {

        BasicDBObject seventies = new BasicDBObject();
        seventies.put("decade", "1970s");
        seventies.put("artist", "张学友");
        seventies.put("song", "吻别");
        seventies.put("weeksAtOne", 10);

        BasicDBObject eighties = new BasicDBObject();
        eighties.put("decade", "1980s");
        eighties.put("artist", "刘德华");
        eighties.put("song", "爱你一万年");
        eighties.put("weeksAtOne", 10);

        BasicDBObject nineties = new BasicDBObject();
        nineties.put("decade", "1990s");
        nineties.put("artist", "郑秀文");
        nineties.put("song", "眉飞色舞");
        nineties.put("weeksAtOne", 16);

        final BasicDBObject[] seedData = {seventies, eighties, nineties};

        return seedData;
    }
}
