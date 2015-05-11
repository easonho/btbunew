package collect.meicai.com.collect.app.db;

/**
 * Coder: 何毅
 * Desc : 新闻标题栏的数据库存储
 * Date : 2015-04-04
 * Time : 15:05
 * Version:1.0
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "btbuNews.db";// 数据库名称
	public static final int VERSION = 1;

	public static final String TABLE_CHANNEL = "news";//数据表

	public static final String ID = "id";//
	public static final String NAME = "name";
	public static final String ORDERID = "orderId";
	public static final String SELECTED = "selected";
	private Context context;
	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext(){
		return context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists "+TABLE_CHANNEL +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				ID + " INTEGER , " +
				NAME + " TEXT , " +
				ORDERID + " INTEGER , " +
				SELECTED + " SELECTED)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
