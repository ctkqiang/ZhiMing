package xin.ctkqiang.database;

import xin.ctkqiang.common.ZhiMingContext;
import xin.ctkqiang.interfaces.DatabaseInterface;
import xin.ctkqiang.interfaces.ZhiMing;

@ZhiMing(debug = true)
public class DatabaseHelper implements DatabaseInterface{
    private String databaseName;
    private String tableName;

    public DatabaseHelper() {
        ZhiMingContext.setDebug(ZhiMingContext.isDebug());
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String getDatabaname() {
        return databaseName;
    }
    
}
