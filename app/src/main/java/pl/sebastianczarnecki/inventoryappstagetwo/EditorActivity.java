package pl.sebastianczarnecki.inventoryappstagetwo;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pl.sebastianczarnecki.inventoryappstagetwo.data.InvContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private EditText productNameET;
    private EditText productPriceET;
    private EditText productQuantityET;
    private Spinner supplieNameSpinner;
    private EditText supplierPhoneET;

    private int mSupplieName = InvContract.InvEntry.SUPPLIER_SPINNER_UNKNOWN;

    private boolean mProductHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.add_product));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        productNameET = findViewById(R.id.product_name_ET);
        productPriceET = findViewById(R.id.product_price_ET);
        productQuantityET = findViewById(R.id.product_quantity_ET);
        supplieNameSpinner = findViewById(R.id.supplier_name_spinner);
        supplierPhoneET = findViewById(R.id.supplier_phone_ET);

        productNameET.setOnTouchListener(mTouchListener);
        productPriceET.setOnTouchListener(mTouchListener);
        productQuantityET.setOnTouchListener(mTouchListener);
        supplieNameSpinner.setOnTouchListener(mTouchListener);
        supplierPhoneET.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter productSupplieNameSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_spinner, android.R.layout.simple_spinner_item);

        productSupplieNameSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        supplieNameSpinner.setAdapter(productSupplieNameSpinnerAdapter);

        supplieNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier_amazon))) {
                        mSupplieName = InvContract.InvEntry.SUPPLIER_SPINNER_AMAZON;
                    } else if (selection.equals(getString(R.string.supplier_allegro))) {
                        mSupplieName = InvContract.InvEntry.SUPPLIER_SPINNER_ALLEGRO;
                    } else if (selection.equals(getString(R.string.supplier_ebay))) {
                        mSupplieName = InvContract.InvEntry.SUPPLIER_SPINNER_EBAY;
                    } else {
                        mSupplieName = InvContract.InvEntry.SUPPLIER_SPINNER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSupplieName = InvContract.InvEntry.SUPPLIER_SPINNER_UNKNOWN;
            }
        });
    }

    private void saveProduct() {
        String productNameString = productNameET.getText().toString().trim();
        String productPriceString = productPriceET.getText().toString().trim();
        String productQuantityString = productQuantityET.getText().toString().trim();
        String productSupplierPhoneNumberString = supplierPhoneET.getText().toString().trim();
        if (mCurrentProductUri == null) {
            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.product_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSupplieName == InvContract.InvEntry.SUPPLIER_SPINNER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_requires), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InvContract.InvEntry.COLUMN_NAME, productNameString);
            values.put(InvContract.InvEntry.COLUMN_PRICE, productPriceString);
            values.put(InvContract.InvEntry.COLUMN_QUANTITY, productQuantityString);
            values.put(InvContract.InvEntry.COLUMN_SUPPLIER_NAME, mSupplieName);
            values.put(InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER, productSupplierPhoneNumberString);

            Uri newUri = getContentResolver().insert(InvContract.InvEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }else{

            if (TextUtils.isEmpty(productNameString)) {
                Toast.makeText(this, getString(R.string.product_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productQuantityString)) {
                Toast.makeText(this, getString(R.string.quantity_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productPriceString)) {
                Toast.makeText(this, getString(R.string.price_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (mSupplieName == InvContract.InvEntry.SUPPLIER_SPINNER_UNKNOWN) {
                Toast.makeText(this, getString(R.string.supplier_name_requires), Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(productSupplierPhoneNumberString)) {
                Toast.makeText(this, getString(R.string.supplier_phone_requires), Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();

            values.put(InvContract.InvEntry.COLUMN_NAME, productNameString);
            values.put(InvContract.InvEntry.COLUMN_PRICE, productPriceString);
            values.put(InvContract.InvEntry.COLUMN_QUANTITY, productQuantityString);
            values.put(InvContract.InvEntry.COLUMN_SUPPLIER_NAME, mSupplieName);
            values.put(InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER, productSupplierPhoneNumberString);

            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InvContract.InvEntry._ID,
                InvContract.InvEntry.COLUMN_NAME,
                InvContract.InvEntry.COLUMN_PRICE,
                InvContract.InvEntry.COLUMN_QUANTITY,
                InvContract.InvEntry.COLUMN_SUPPLIER_NAME,
                InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            String currentName = cursor.getString(nameColumnIndex);
            int currentPrice = cursor.getInt(priceColumnIndex);
            int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            productNameET.setText(currentName);
            productPriceET.setText(Integer.toString(currentPrice));
            productQuantityET.setText(Integer.toString(currentQuantity));
            supplierPhoneET.setText(Integer.toString(currentSupplierPhone));

            switch (currentSupplierName) {
                case InvContract.InvEntry.SUPPLIER_SPINNER_AMAZON:
                    supplieNameSpinner.setSelection(1);
                    break;
                case InvContract.InvEntry.SUPPLIER_SPINNER_ALLEGRO:
                    supplieNameSpinner.setSelection(2);
                    break;
                case InvContract.InvEntry.SUPPLIER_SPINNER_EBAY:
                    supplieNameSpinner.setSelection(3);
                    break;
                default:
                    supplieNameSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productNameET.getText().clear();
        productPriceET.getText().clear();
        productQuantityET.getText().clear();
        supplierPhoneET.getText().clear();
        supplieNameSpinner.setSelection(0);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}