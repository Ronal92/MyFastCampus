package com.jinwoo.android.databasebasic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jinwoo.android.databasebasic.domain.Bbs;
import com.jinwoo.android.databasebasic.domain.Memo;

import java.sql.SQLException;

/**
 * Created by JINWOO on 2017-02-14.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "database.db"; // 테이블이 담길 파일
    public static final int DB_VERSION = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);

    }

    /**
     * 생성자에서 호출되는 super(context..... 에서 database.db 파일이 생성되어 있지 않으면 호출된다.
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // Bbs.class 파일에 정의된 테이블을 생성한다.
            // 객체를 테이블로 변환시켜준다.
            TableUtils.createTable(connectionSource, Bbs.class);
            TableUtils.createTable(connectionSource, Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 생성자에서 호출되는 super(context..... 에서 database.db 파일이 존재하지만 DB_VERSION이 증가되면 호출된다.
     * 테이블의 컬럼 사항(컬럼명, 컬럼 타입, 컬럼 수 등)이 변경되는 경우.
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // Bbs.class에 정의된 테이블 삭제
            TableUtils.dropTable(connectionSource, Bbs.class, false);
            // 데이터를 보존해야 될 필요성이 있으면 중간처리를 해줘야만 한다.
            // TODO : 임시테이블을 생성한 후 데이터를 먼저 저장하고 onCreate 이후에 데이터를 입력해준다.
            // onCreate를 호출해서 테이블을 생성해준다.
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     * DBHelper를 싱글턴으로 사용하기 때문에 dao 객체도 열어놓고 사용가능하다.
     */
    private Dao<Bbs, Long> bbsDao = null;
    public Dao<Bbs, Long> getBbsDao() throws  SQLException{
        if(bbsDao == null){
            bbsDao = getDao(Bbs.class);
        }
            return bbsDao;
    }

    public void releaseBbsDao(){
        if(bbsDao != null){
            bbsDao = null;
        }
    }
}
