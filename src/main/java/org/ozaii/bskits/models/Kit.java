package org.ozaii.bskits.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@DatabaseTable(tableName = "kits")
public class Kit {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(columnName = "created_by", canBeNull = false)
    private UUID createdBy;

    @DatabaseField(columnName = "created_at", canBeNull = false)
    private long createdAt;

    public Kit() {} // for ormLite (dont delete)

    public Kit(String name, UUID createdBy) {
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = System.currentTimeMillis();
    }

}

