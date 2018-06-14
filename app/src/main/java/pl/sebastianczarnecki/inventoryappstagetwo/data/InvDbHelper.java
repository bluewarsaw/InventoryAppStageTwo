package pl.sebastianczarnecki.inventoryappstagetwo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InvDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InvDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PRODUCT_TABLE = "CREATE TABLE " + InvContract.InvEntry.TABLE_NAME + " ("
                + InvContract.InvEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InvContract.InvEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + InvContract.InvEntry.COLUMN_PRICE + " INTEGER NOT NULL, "
                + InvContract.InvEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + InvContract.InvEntry.COLUMN_SUPPLIER_NAME + " INTEGER NOT NULL DEFAULT 0, "
                + InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER );";

        db.execSQL(SQL_CREATE_PRODUCT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InvContract.InvEntry.TABLE_NAME);
    }
}
