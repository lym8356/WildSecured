package com.fit5046.wildsecured.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Item", foreignKeys = {
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE
        )
})
public class Item {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "itemName")
    public String itemName;

    @ColumnInfo(name = "categoryId")
    public int categoryId;

    @ColumnInfo(name = "isChecked")
    public boolean isChecked;

    public Item(String itemName, int categoryId, boolean isChecked) {
        this.itemName = itemName;
        this.categoryId = categoryId;
        this.isChecked = isChecked;
    }
}
