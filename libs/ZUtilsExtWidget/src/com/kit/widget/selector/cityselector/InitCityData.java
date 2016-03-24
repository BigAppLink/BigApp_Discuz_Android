package com.kit.widget.selector.cityselector;

import android.content.Context;

import com.kit.utils.ListUtils;
import com.kit.utils.StringUtils;

import java.util.List;

public class InitCityData {

    private Context mContext;

    public InitCityData(Context context) {
        this.mContext = context;
    }

    public List<Place> initCityFromLocal(String pcode, boolean isHaveDefaultAll, String defaultAllName) {

        DBPlace dbPlace = new DBPlace(mContext);
        dbPlace.open();

        List<Place> list = dbPlace
                .selectByName4List(DBPlace.FIELD_PCODE, pcode);


        // try {
        //
        // Cursor cursor = dbPlace.selectByName(DBPlace.FIELD_PCODE, pcode);
        // cursor.moveToFirst();
        // while (!cursor.isLast()) {
        // String code = cursor.getString(cursor
        // .getColumnIndex(DBPlace.FIELD_CODE));
        // byte bytes[] = cursor.getBlob(2);
        // String name = new String(bytes, "utf-8");
        // Place myListItem = new Place();
        // myListItem.setName(name);
        // myListItem.setPcode(code);
        // list.add(myListItem);
        // cursor.moveToNext();
        // }
        // String code = cursor.getString(cursor
        // .getColumnIndex(DBPlace.FIELD_CODE));
        // byte bytes[] = cursor.getBlob(2);
        // String name = new String(bytes, "utf-8");
        // Place myListItem = new Place();
        // myListItem.setName(name);
        // myListItem.setPcode(code);
        // list.add(myListItem);
        //
        // } catch (Exception e) {
        // }

        dbPlace.close();

        if (isHaveDefaultAll) {

            Place place = new Place();
            if (!StringUtils.isNullOrEmpty(defaultAllName)) {
                place.setName(defaultAllName);
            } else {
                place.setName("全境");
            }

            place.setLevel(4);

            list = ListUtils.addTop(list, place);
        }
        return list;

    }

}
