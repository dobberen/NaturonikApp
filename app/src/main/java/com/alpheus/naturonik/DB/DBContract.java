package com.alpheus.naturonik.DB;

import android.provider.BaseColumns;

public class DBContract {

    public static final class ProductEntry implements BaseColumns{

        public static final String TABLE_NAME = "products";

        public static final String COLUMN_COUNTRY = "countrys_name";
        public static final String COLUMN_SORT = "sorts_name";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TYPE_PACK = "type_pack";
        public static final String COLUMN_PRICE = "price";
    }
}
