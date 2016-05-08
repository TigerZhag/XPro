package github.tiger;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class daogenerator {
    private static int DATABASE_VERSION = 1;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(DATABASE_VERSION,"github.tiger.greendao");
        
        addKey(schema);
        new DaoGenerator().generateAll(schema,"E:/Androidstudio/workspace/XPro/xfile/src/main/java-gen");
    }

    private static void addKey(Schema schema) {
        Entity key = schema.addEntity("Key");
        key.setTableName("Key");
        key.addIdProperty().primaryKey().autoincrement();
        key.addStringProperty("path").notNull();
        key.addStringProperty("key").notNull();
    }
}
