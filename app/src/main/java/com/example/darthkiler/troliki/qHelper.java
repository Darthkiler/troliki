package com.example.darthkiler.troliki;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Aylar-HP on 12/10/2015.
 */
public class qHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "trol";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 7;


    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public qHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            /*Connection con;
            Statement stmt;
            ResultSet rs;
            String url ="jdbc:postgresql://192.168.1.220:5432/postgres";
            String log="postgres";
            String pass="123";
            try {
                Class.forName("org.postgresql.Driver");
                con= DriverManager.getConnection(url, log, pass);
                stmt = con.createStatement();
                rs = stmt.executeQuery("select statie,cod,trol,ctime from oprire");
                while (rs.next()) {
                    mDataBase.rawQuery("insert into oprire values('"+rs.getString(1)+"',"+rs.getInt(1)+","+rs.getInt(2)+","+rs.getInt(3)+");",null);

                }
                rs = stmt.executeQuery("select ctimp,urm from timp");
                while (rs.next()) {
                    mDataBase.rawQuery("insert into tinp values("+rs.getInt(1)+","+rs.getInt(2)+");",null);

                }
                rs = stmt.executeQuery("select cod,ora,min,zi from orar");
                while (rs.next()) {
                    mDataBase.rawQuery("insert into orar values("+rs.getInt(1)+","+rs.getInt(2)+","+rs.getInt(3)+",'"+rs.getString(1)+"');",null);

                }
                rs = stmt.executeQuery("select name,password from login");
                while (rs.next()) {
                    mDataBase.rawQuery("insert into login values('"+rs.getString(1)+"','"+rs.getString(2)+"');",null);

                }
                rs.close();

            }
            catch (Exception e){
            }*/
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        //InputStream mInput = mContext.getAssets().open(DB_NAME);
        InputStream mInput = mContext.getResources().openRawResource(R.raw.trol);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

}