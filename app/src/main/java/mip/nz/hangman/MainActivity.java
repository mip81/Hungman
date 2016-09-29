package mip.nz.hangman;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String[] arrCat = {"Cities", "Vegetables", "Fruits", "Cars"};
    ListView lvCategories ;
    ArrayAdapter<String> adapter;

    // Column which will bind
    String[] arrCols = {DBHelper.WORDS_COLUMN_CAT};
    // View which get the data
    int[] arrViews = {R.id.tvCatSCA};
    SimpleCursorAdapter sca ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        final DBHelper db = new DBHelper(getApplicationContext());
        //Get adapter from array;
        //   arrCat = db.getCategories().toArray(new String[arrCat.length]);
        //  adapter = new ArrayAdapter<String>(this, R.layout.activity_category, arrCat);


        // FILL LISTVIEW FROM DB (Context)
        sca = new SimpleCursorAdapter(this, R.layout.activity_category_sca, db.getCursorCategories(), arrCols, arrViews, 0);


        lvCategories = (ListView)findViewById(R.id.lvCategories);
        lvCategories.setAdapter(sca);    // from array lvCategories.setAdapter(adapter);
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = ((Cursor)sca.getItem(position)).getString(0);
                String randomWord = db.getRandomWordFromCategory(category);

                  //  db.loadBase();
                Intent game  = new Intent(getApplicationContext(), GameActivity.class);
                game.putExtra("word", randomWord);
                startActivity(game);

                //Toast.makeText(getApplicationContext(), "Random word :  "+randomWord, Toast.LENGTH_LONG).show();

            }
        });



    }



}
