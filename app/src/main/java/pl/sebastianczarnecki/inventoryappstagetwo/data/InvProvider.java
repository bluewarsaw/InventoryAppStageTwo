package pl.sebastianczarnecki.inventoryappstagetwo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class InvProvider extends ContentProvider {

    private static final int PROUDCTS = 100;
    private static final int PROUDCT_ID = 101;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(InvContract.CONTENT_AUTHORITY, InvContract.PATH_INVENTORY, PROUDCTS);

        mUriMatcher.addURI(InvContract.CONTENT_AUTHORITY, InvContract.PATH_INVENTORY + "/#", PROUDCT_ID);
    }

    private InvDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new InvDbHelper((getContext()));
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = mUriMatcher.match(uri);
        switch (match) {
            case PROUDCTS:
                cursor = database.query(InvContract.InvEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PROUDCT_ID:
                selection = InvContract.InvEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(InvContract.InvEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PROUDCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Not supported insert for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PROUDCTS:
                return InvContract.InvEntry.CONTENT_LIST_TYPE;
            case PROUDCT_ID:
                return InvContract.InvEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI" + uri + " with match " + match);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        String nameProduct = values.getAsString(InvContract.InvEntry.COLUMN_NAME);
        if (nameProduct == null) {
            throw new IllegalArgumentException("Please fill up Product name");
        }

        Integer priceProduct = values.getAsInteger(InvContract.InvEntry.COLUMN_PRICE);
        if (priceProduct != null && priceProduct < 0) {
            throw new IllegalArgumentException("Please fill up Product price");
        }

        Integer quantityProduct = values.getAsInteger(InvContract.InvEntry.COLUMN_QUANTITY);
        if (quantityProduct != null && quantityProduct < 0) {
            throw new IllegalArgumentException("Please fill up Product quantity");
        }

        Integer supplierName = values.getAsInteger(InvContract.InvEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null || !InvContract.InvEntry.isValidSupplierName(supplierName)) {
            throw new IllegalArgumentException("Please fill up Pets gender");
        }

        Integer supplierPhone = values.getAsInteger(InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (supplierPhone != null && supplierPhone < 0) {
            throw new IllegalArgumentException("Please fill up Supplier Phone");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(InvContract.InvEntry.TABLE_NAME, null, values);
        if (id == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PROUDCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PROUDCT_ID:
                selection = InvContract.InvEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PROUDCTS:
                rowsDeleted = database.delete(InvContract.InvEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PROUDCT_ID:
                selection = InvContract.InvEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(InvContract.InvEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(InvContract.InvEntry.COLUMN_NAME)) {
            String nameProduct = values.getAsString(InvContract.InvEntry.COLUMN_NAME);
            if (nameProduct == null) {
                throw new IllegalArgumentException("Please fill up Product name");
            }
        }
        if (values.containsKey(InvContract.InvEntry.COLUMN_PRICE)) {
            Integer priceProduct = values.getAsInteger(InvContract.InvEntry.COLUMN_PRICE);
            if (priceProduct != null && priceProduct < 0) {
                throw new
                        IllegalArgumentException("Please fill up Product price");
            }
        }

        if (values.containsKey(InvContract.InvEntry.COLUMN_QUANTITY)) {
            Integer quantityProduct = values.getAsInteger(InvContract.InvEntry.COLUMN_QUANTITY);
            if (quantityProduct != null && quantityProduct < 0) {
                throw new
                        IllegalArgumentException("Please fill up Product quantity");
            }
        }
        if (values.containsKey(InvContract.InvEntry.COLUMN_SUPPLIER_NAME)) {
            Integer supplierName = values.getAsInteger(InvContract.InvEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null || !InvContract.InvEntry.isValidSupplierName(supplierName)) {
                throw new IllegalArgumentException("Please fill up Supplier Name");
            }
        }

        if (values.containsKey(InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            Integer supplierPhone = values.getAsInteger(InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierPhone != null && supplierPhone < 0) {
                throw new
                        IllegalArgumentException("Please fill up Supplier Phone");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(InvContract.InvEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}