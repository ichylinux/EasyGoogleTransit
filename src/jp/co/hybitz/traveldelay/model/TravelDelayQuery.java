package jp.co.hybitz.traveldelay.model;

import java.io.Serializable;

public class TravelDelayQuery implements Serializable {
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
}
