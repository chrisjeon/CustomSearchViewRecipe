package com.toasterbits.customsearchviewrecipe;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by chrisjeon on 1/23/17.
 */

public class SearchHistory extends SugarRecord {
    @Unique
    public String title;

    public SearchHistory() {}

    public SearchHistory(String query) {
        this.title = query;
    }

    public String getTitle() {
        return title;
    }
}
