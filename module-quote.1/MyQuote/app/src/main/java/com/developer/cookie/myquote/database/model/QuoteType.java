package com.developer.cookie.myquote.database.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Realm model class for table named "type".
 * It helps get and set type for every quote.
 * Example for quote type: "Book quote", "MyQuote".
 */
public class QuoteType extends RealmObject {
    private long id;
    @Required
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}