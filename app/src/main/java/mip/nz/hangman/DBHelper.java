package mip.nz.hangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mikhailpastushkov on 9/28/16.
 */

public class DBHelper extends SQLiteOpenHelper {
    public final boolean DEBUG = true;
    public static final String DATABASE_NAME = "hungman.db";
    public static final int VER = 5;

    public static final String TABLE_NAME_WORDS = "words";

    public static final String WORDS_COLUMN_ID = "_id";
    public static final String WORDS_COLUMN_CAT = "_cat";
    public static final String WORDS_COLUMN_WORD = "_word";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VER);
        Log.i("DB path :", (context.getDatabasePath(DATABASE_NAME)).getAbsolutePath() );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {




        db.execSQL("CREATE TABLE "+TABLE_NAME_WORDS+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+WORDS_COLUMN_CAT+" text,"
                +WORDS_COLUMN_WORD+" text)");

        //ContentValues cv = new ContentValues();
        //cv.put("City", );

        db.execSQL("INSERT INTO "+TABLE_NAME_WORDS+ "("+WORDS_COLUMN_ID+","+WORDS_COLUMN_CAT+", "+WORDS_COLUMN_WORD+") VALUES (1,'Cities','Auckland')" );
        db.execSQL("INSERT INTO "+TABLE_NAME_WORDS+ "("+WORDS_COLUMN_ID+","+WORDS_COLUMN_CAT+", "+WORDS_COLUMN_WORD+") VALUES (2,'Cars','Subaru')" );
        db.execSQL("INSERT INTO "+TABLE_NAME_WORDS+ "("+WORDS_COLUMN_ID+","+WORDS_COLUMN_CAT+", "+WORDS_COLUMN_WORD+") VALUES (3,'Fruits','Auckland')" );
        db.execSQL("INSERT INTO "+TABLE_NAME_WORDS+ "("+WORDS_COLUMN_ID+","+WORDS_COLUMN_CAT+", "+WORDS_COLUMN_WORD+") VALUES (4,'Vegetables','Potato')" );

        loadBase();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+TABLE_NAME_WORDS;
        db.execSQL(sql);
        onCreate(db);
    }


    /**
     * Read DB and return list of categories
     *
     * @return ArrayList of Categories
     */
    public ArrayList<String> getCategories(){
        ArrayList<String> listCat = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+WORDS_COLUMN_ID+" ,DISTINCT("+WORDS_COLUMN_CAT+") FROM words", null);

        if(cursor != null){
            if(cursor.moveToFirst()){

                //fetch data from db
                while (cursor.isAfterLast() == false){
                    listCat.add(cursor.getString(0));
                    if(DEBUG) Log.i("DEBUG  get CATLIST : ", cursor.getString(0));

                    cursor.moveToNext();

                }
            }
        }
        db.close();

        return listCat;
    }


    /**
     *  GET CURSOR OF CATEGORIES
     */

    public Cursor getCursorCategories(){
        SQLiteDatabase db = getReadableDatabase();
        return  db.rawQuery("SELECT DISTINCT w."+WORDS_COLUMN_CAT+", (SELECT _id FROM "+TABLE_NAME_WORDS+" WHERE "+WORDS_COLUMN_CAT+" = w._cat) as _id FROM "+TABLE_NAME_WORDS+" as w", null);
    }


    public void loadBase(){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        String[] veg  = {"asparagus"
                ,"beans"
                ,"beet"
                ,"broccoli"
                ,"cabbage"
                ,"carrot"
                ,"cauliflower"
                ,"celery"
                ,"Chinese"
                ,"corn"
                ,"cucumber"
                ,"eggplant"
                ,"kale"
                ,"lettuce"
                ,"okra"
                ,"onion"
                ,"peas"
                ,"potato"
                ,"pumpkins"
                ,"radish"
                ,"spinach"
                ,"tomatoes"
                ,"turnips"
        };

        for(String v : veg){
            cv.put(WORDS_COLUMN_CAT,"Vegetables");
            cv.put(WORDS_COLUMN_WORD, v);
            db.insert(TABLE_NAME_WORDS, null, cv);

        }

        String[] animal = {
                "alligator"
                ,"ant"
                ,"bear"
                ,"bird"
                ,"camel"
                ,"cat"
                ,"cheetah"
                ,"chicken"
                ,"chimpanzee"
                ,"cow"
                ,"crocodile"
                ,"deer"
                ,"dog"
                ,"dolphin"

        };

        for(String a : animal){
            cv.put(WORDS_COLUMN_CAT,"Animals");
            cv.put(WORDS_COLUMN_WORD, a);
            db.insert(TABLE_NAME_WORDS, null, cv);

        }

        String[] jobs = {
        "accountant"
                ,"actor"
        ,"actress"
                ,"athlete"
        ,"author"
                ,"baker"
        ,"banker"
                ,"barber"
        ,"beautician"
                ,"broker"
        ,"burglar"
                ,"butcher"
        ,"carpenter"
                ,"chauffeur"
        ,"chef"
                ,"clerk"
        ,"coach"
                ,"craftsman"};

        for(String j : jobs){
            cv.put(WORDS_COLUMN_CAT,"Jobs");
            cv.put(WORDS_COLUMN_WORD, j);
            db.insert(TABLE_NAME_WORDS, null, cv);

        }


        String[] clothes = {
            "belt"
            ,"blouse"
            ,"boots"
            ,"cap"
            ,"cardigan"
            ,"coat"
            ,"dress"
            ,"gloves"
            ,"hat"
            ,"jacket"


    };

        for(String c : clothes){
            cv.put(WORDS_COLUMN_CAT,"Clothes");
            cv.put(WORDS_COLUMN_WORD, c);
            db.insert(TABLE_NAME_WORDS, null, cv);

        }


        String[] shapes ={

                "circle"
                ,"cone"
                ,"cylinder"
                ,"ellipse"
                ,"hexagon"
                ,"irregular shape"
                ,"octagon"
                ,"oval"
                ,"parallelogram"
                ,"pentagon"
                ,"pyramid"
        };

        for(String s : shapes){
            cv.put(WORDS_COLUMN_CAT,"Shapes");
            cv.put(WORDS_COLUMN_WORD, s);
            db.insert(TABLE_NAME_WORDS, null, cv);

        }




        //db.insert();

    }


    // Get random cat and then random random word
    public String randomWord(){
        Random radom = new Random();
        String randomCat , randomWord;
        ArrayList<String> alCats = new ArrayList<String>();
        ArrayList<String> alWors = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT DISTINCT("+WORDS_COLUMN_CAT+") FROM "+TABLE_NAME_WORDS;
        Cursor cursor = db.rawQuery(sql, null);

            if(cursor != null){
                if(cursor.moveToFirst()){

                    while(!cursor.isAfterLast())
                        alCats.add(cursor.getString(0));
                        cursor.moveToNext();
                }

            }
        randomCat = alCats.get( radom.nextInt(alCats.size()-1) );

        sql = "SELECT "+WORDS_COLUMN_WORD+" FROM "+TABLE_NAME_WORDS+" WHERE "+WORDS_COLUMN_CAT+" = '"+randomCat+"'";

        cursor = null;
        cursor = db.rawQuery(sql, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    alWors.add(cursor.getString(0));
                    cursor.moveToNext();
                }
            }
        }
        randomWord = alWors.get(radom.nextInt(alWors.size()-1));

        return randomWord;
    }


    // Get the word randomly from category

    public String getRandomWordFromCategory(String cat){
        SQLiteDatabase db = getReadableDatabase();
        List<String> al = new ArrayList<>();
        Random random = new Random();
        int rndPos = 0;
        String result = "";

        Cursor cursor = db.rawQuery("SELECT "+WORDS_COLUMN_WORD+" FROM "+TABLE_NAME_WORDS+" WHERE "+WORDS_COLUMN_CAT+" = '"+cat+"'", null);
            if(cursor != null){

                if(cursor.moveToFirst()){
                    while(!cursor.isAfterLast()){

                        al.add(cursor.getString(0));
                        cursor.moveToNext();
                    }

                }

            }

        if(DEBUG) Log.d("DEBUG al of words : ", al.toString());
        // check if list has only element return 1 otherwise will folow exception
        rndPos =  (al.size()==1)? 0 : random.nextInt(al.size()-1);
        if(DEBUG) Log.d("DEBUG :  : ","getRandomWordFromCategory() rndPos in al :  "+rndPos+" word : "+al.get(rndPos));
        return al.get(rndPos).toUpperCase();

    }



}
