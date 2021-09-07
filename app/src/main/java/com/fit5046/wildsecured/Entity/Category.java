package com.fit5046.wildsecured.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Category", foreignKeys = {
        @ForeignKey(
                entity = UserList.class,
                parentColumns = "id",
                childColumns = "listId",
                onDelete = ForeignKey.CASCADE
        )
})
public class Category {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "categoryName")
    public String categoryName;

    @ColumnInfo(name = "listId")
    public int listId;

    @ColumnInfo(name = "isExpanded")
    public boolean isExpanded;

    public Category(String categoryName, int listId) {
        this.categoryName = categoryName;
        this.listId = listId;
        this.isExpanded = true;
    }
}
