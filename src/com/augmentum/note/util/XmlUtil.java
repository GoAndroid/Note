package com.augmentum.note.util;

import android.os.Environment;
import com.augmentum.note.model.Note;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

   public static final String SD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
   public static final String APP_DIR = "note_gavin";
   public static final String BACKUP_FILE = "note.xml";
   public static final String FILE_NAME = SD_ROOT_PATH + File.pathSeparator + APP_DIR + File.pathSeparator + BACKUP_FILE;

   public static List<Note> parse() {

       List<Note> result = new ArrayList<Note>();

       try {
           InputStream inputStream = new FileInputStream(FILE_NAME);
           XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
           XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
       } catch (XmlPullParserException e) {
           e.printStackTrace();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       }

       return result;
   }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }
}
