package com.toficer.data;

import javafx.scene.layout.VBox;

public interface DisplayableInListView {

    // returns a description box for the given item
    // which is used to display the item's details
    // when it's selected from the ListView
    VBox getDescriptionBox();

}
