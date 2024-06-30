package com.example.smartlockjava.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class PersonTableManager {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public interface OnPersonsLoadedCallback {
        void onPersonsLoaded(List<Person> persons);
    }

    public PersonTableManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public Person findPersonByVector(float[] vector) {
        byte[] byteArray = toByteArray(vector);
        String whereClause = DatabaseHelper.FACE_VECTOR + " = ?";
        String[] whereArgs = new String[]{new String(byteArray)};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PERSON, null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USERNAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD));
            @SuppressLint("Range") byte[] faceVectorBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.FACE_VECTOR));
            float[] faceVector = toFloatArray(faceVectorBytes);
            cursor.close();
            return new Person(username, password, faceVector);
        }
        return null;
    }

    public Person findPersonByUsername(String usernameInput) {
        String whereClause = DatabaseHelper.USERNAME + " = ?";
        String[] whereArgs = new String[]{usernameInput};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PERSON, null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USERNAME));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD));
            @SuppressLint("Range") byte[] faceVectorBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.FACE_VECTOR));
            float[] faceVector = toFloatArray(faceVectorBytes);
            cursor.close();
            return new Person(username, password, faceVector);
        }
        return null;
    }

    public void changePersonPassword(String username, String newPassword) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PASSWORD, newPassword);
        String whereClause = DatabaseHelper.USERNAME + " = ?";
        String[] whereArgs = new String[]{username};
        database.update(DatabaseHelper.TABLE_PERSON, values, whereClause, whereArgs);
    }

    public List<Person> findAllPersons() {
        List<Person> persons = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_PERSON, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                float[] faceVector = null;
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USERNAME));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD));
                @SuppressLint("Range") byte[] faceVectorBytes = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.FACE_VECTOR));
                if (faceVectorBytes != null) {
                    faceVector = toFloatArray(faceVectorBytes);
                }
                persons.add(new Person(username, password, faceVector));
                Log.d("PersonTableManager", "findAllPersons: " + persons.size());
            }
            cursor.close();
        }
        return persons;
    }

    public void savePerson(Person person) {
        float[] faceVector = person.getFaceVector();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USERNAME, person.getUsername());
        values.put(DatabaseHelper.PASSWORD, person.getPassword());
        if (faceVector != null) {
            values.put(DatabaseHelper.FACE_VECTOR, toByteArray(faceVector));
        } else {
            values.putNull(DatabaseHelper.FACE_VECTOR);
        }
        database.insert(DatabaseHelper.TABLE_PERSON, null, values);
    }

    private byte[] toByteArray(float[] floatArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(floatArray.length * 4);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(floatArray);
        return byteBuffer.array();
    }

    private float[] toFloatArray(byte[] byteArray) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        float[] floatArray = new float[floatBuffer.capacity()];
        floatBuffer.get(floatArray);
        return floatArray;
    }
}