package pl.sebastianczarnecki.inventoryappstagetwo.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InvContract {

    public static final String CONTENT_AUTHORITY =  "pl.sebastianczarnecki.inventoryappstagetwo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_INVENTORY = "product";

    public InvContract() {
    }

    public final static class InvEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +  PATH_INVENTORY;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public final static String TABLE_NAME = "product";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "product_name";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        public final static int SUPPLIER_SPINNER_UNKNOWN = 0;
        public final static int SUPPLIER_SPINNER_AMAZON = 1;
        public final static int SUPPLIER_SPINNER_ALLEGRO = 2;
        public final static int SUPPLIER_SPINNER_EBAY = 3;

        public static boolean isValidSupplierName(int suppliername) {
            if (suppliername == SUPPLIER_SPINNER_UNKNOWN || suppliername == SUPPLIER_SPINNER_AMAZON || suppliername == SUPPLIER_SPINNER_ALLEGRO || suppliername == SUPPLIER_SPINNER_EBAY) {
                return true;
            }
            return false;
        }
    }
}
