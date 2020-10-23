import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.database.sqlite.SQLiteDatabase.openDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addbook(View v){
        SQLiteDatabase myDB =  null;
        String tableName = "booksimple";
        String insertSQL = "OOO";
        TextView r = (TextView) findViewById(R.id.result);
        /* Create the Database if it does not exist */
        try {
            r.setText("got here 0");
            myDB = openOrCreateDatabase("simplebooks.db", MODE_PRIVATE, null);
            r.setText("got here 1");
            /* Create a table in the database
             */
            myDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                    " (Title VARCHAR, " +
                    "Author VARCHAR, " +
                    "Year INT);");
            r.setText("got here 2");

            /* add the new book to the database  - note not checking for duplicates " */
            EditText titlein = (EditText) findViewById(R.id.booktitle);
            String bTitle = titlein.getText().toString();

            EditText authorin = (EditText) findViewById(R.id.bookauthor);
            String bAuthor = authorin.getText().toString();

            EditText yearin = (EditText) findViewById(R.id.yearpublished);
            String yearinput = yearin.getText().toString();
            int bYear = Integer.parseInt(yearinput);
            r.setText("got here 3");

            insertSQL = "INSERT INTO " + tableName +
                    " (Title,Author,Year)" +
                    " VALUES('" + bTitle + "', '" + bAuthor + "', " + bYear + ");";
            r.setText("insertSQL is " + insertSQL);
            myDB.execSQL(insertSQL);

            r.setText("Book has been added to the database");

            myDB.close();
        }
        catch (SQLiteException e) {
            r.setText(r.getText() + "Problem adding to the database");
        }
    }

    public void displaybooks(View v) {
        String dbInfo = "";
        TextView list = (TextView) findViewById(R.id.result);
        try {
            SQLiteDatabase myDB = SQLiteDatabase.openDatabase(
                    "/data/data/" + getPackageName() +
                            "/databases/simplebooks.db",
                    null,
                    SQLiteDatabase.OPEN_READONLY);

            String q = "SELECT * FROM booksimple;";
            Cursor crs = myDB.rawQuery(q, null);
            if (crs.moveToFirst()) {
                // ok at least one record in the database
                do {
                    dbInfo += crs.getString(0) + "\n" +
                            crs.getString(1) + "\n" +
                            crs.getInt(2) + "\n\n";
                } while(crs.moveToNext());

                myDB.close();
            }


        } catch (SQLiteException e) {
            dbInfo += "database does not yet exist";
        }

        list.setText(list.getText() + dbInfo);
    }
}
