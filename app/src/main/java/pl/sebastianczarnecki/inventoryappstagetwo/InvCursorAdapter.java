package pl.sebastianczarnecki.inventoryappstagetwo;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import pl.sebastianczarnecki.inventoryappstagetwo.data.InvContract;

public class InvCursorAdapter extends CursorAdapter {

    public InvCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView productNameTV = view.findViewById(R.id.product_name_TV);
        TextView productPriceTV = view.findViewById(R.id.product_price_TV);
        TextView productQuantityTV = view.findViewById(R.id.product_quantity_TV);
        Button productSaleButton = view.findViewById(R.id.sale_button);

        final int columnIdIndex = cursor.getColumnIndex(InvContract.InvEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(InvContract.InvEntry.COLUMN_QUANTITY);

        final String productID = cursor.getString(columnIdIndex);
        String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);

        productNameTV.setText(productName);
        productPriceTV.setText(context.getString(R.string.product_price) + " : " + productPrice);
        productQuantityTV.setText(context.getString(R.string.product_quantity) + " : " + productQuantity);

        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InvActivity Activity = (InvActivity) context;
                Activity.productSaleCount(Integer.valueOf(productID), Integer.valueOf(productQuantity));
            }
        });


        Button productEditButton = view.findViewById(R.id.edit_button);
        productEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EditorActivity.class);
                Uri currentProdcuttUri = ContentUris.withAppendedId(InvContract.InvEntry.CONTENT_URI, Long.parseLong(productID));
                intent.setData(currentProdcuttUri);
                context.startActivity(intent);
            }
        });
    }
}