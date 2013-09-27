package com.augmentum.note.util;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import com.augmentum.note.R;
import com.augmentum.note.dao.NoteDao;
import com.augmentum.note.dao.impl.NoteDaoImpl;
import com.augmentum.note.model.Note;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

    public static final String TAG = "XmlUtil";

    public static final String SD_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APP_DIR = "note_gavin";
    public static final String BACKUP_FILE = "note.xml";
    public static final String FILE_DIR = SD_ROOT_PATH + File.separator + APP_DIR;
    public static final String FILE_NAME = FILE_DIR + File.separator + BACKUP_FILE;
    public static final String NEW_LINE = "\n";

    public static List<Note> deserialize() {
        List<Note> result = new ArrayList<Note>();

        if (!isExternalStorageReadable()) {
            Log.v(TAG, "isExternalStorageReadable = false");
            return result;
        }

        try {
            FileInputStream fis = new FileInputStream(FILE_NAME);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, "utf-8");
            Note noteTemp = null;

            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (parser.getEventType()) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.v(TAG, "start document");
                        break;
                    case XmlPullParser.START_TAG:
                        Log.v(TAG, parser.getName() + " : " + "start tag");

                        if (parser.getName().equals(Note.NoteEntry.TABLE_NAME)) {
                            noteTemp = new Note();
                        } else if (parser.getName().equals(Note.NoteEntry._ID)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setId(Long.parseLong(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_TYPE)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setType(Integer.parseInt(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_PARENT_ID)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setParentId(Long.parseLong(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_COLOR)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setColor(Integer.parseInt(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_CREATE_TIME)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setCreateTime(Long.parseLong(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_MODIFY_TIME)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setModifyTime(Long.parseLong(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_ALERT_TIME)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setAlertTime(Long.parseLong(parser.getText()));
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_CONTENT)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setContent(parser.getText());
                            }
                        } else if (parser.getName().equals(Note.NoteEntry.COLUMN_NAME_SUBJECT)) {
                            if (noteTemp != null) {
                                parser.next();
                                noteTemp.setSubject(parser.getText());
                            }
                        }

                        break;
                    case XmlPullParser.END_TAG:
                        Log.v(TAG, parser.getName() + " : " + "end tag");

                        if (parser.getName().equals(Note.NoteEntry.TABLE_NAME)) {

                            if (null != noteTemp) {
                                result.add(noteTemp);
                            }

                            noteTemp = null;
                        }

                        break;
                }

                parser.next();
            }
            Log.v(TAG, "end document");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void serialize() {
        Log.v(TAG, FILE_NAME);

        if (!isExternalStorageWritable()) {
            Log.v(TAG, "isExternalStorageWriteable = false");
            return;
        }

        if (makeDir()) {
            return;
        };

        NoteDao noteDao = NoteDaoImpl.getInstance();
        List<Note> notes = noteDao.getAll();
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(FILE_NAME);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(fos, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "notes");

            for (Note note : notes) {
                xmlSerializer.startTag(null, Note.NoteEntry.TABLE_NAME);

                xmlSerializer.startTag(null, Note.NoteEntry._ID);
                xmlSerializer.text(String.valueOf(note.getId()));
                xmlSerializer.endTag(null, Note.NoteEntry._ID);

                xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_PARENT_ID);
                xmlSerializer.text(String.valueOf(note.getParentId()));
                xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_PARENT_ID);

                xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_TYPE);
                xmlSerializer.text(String.valueOf(note.getType()));
                xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_TYPE);

                xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_CREATE_TIME);
                xmlSerializer.text(String.valueOf(note.getCreateTime()));
                xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_CREATE_TIME);

                if (Note.TYPE_NOTE == note.getType()) {
                    xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_COLOR);
                    xmlSerializer.text(String.valueOf(note.getColor()));
                    xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_COLOR);

                    xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_MODIFY_TIME);
                    xmlSerializer.text(String.valueOf(note.getModifyTime()));
                    xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_MODIFY_TIME);

                    xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_ALERT_TIME);
                    xmlSerializer.text(String.valueOf(note.getAlertTime()));
                    xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_ALERT_TIME);

                    xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_CONTENT);
                    xmlSerializer.text(note.getContent());
                    xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_CONTENT);
                } else {
                    xmlSerializer.startTag(null, Note.NoteEntry.COLUMN_NAME_SUBJECT);
                    xmlSerializer.text(note.getSubject());
                    xmlSerializer.endTag(null, Note.NoteEntry.COLUMN_NAME_SUBJECT);
                }

                xmlSerializer.endTag(null, Note.NoteEntry.TABLE_NAME);
            }

            xmlSerializer.endTag(null, "notes");
            xmlSerializer.endDocument();
        } catch (FileNotFoundException e) {
            Log.v(TAG, "FileNotFound");
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG, "IOException");
            e.printStackTrace();
        } finally {

            try {

                if (fos != null) {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static boolean makeDir() {
        File file = new File(FILE_DIR);

        return file.exists() || file.mkdirs();
    }

    public static String exportToTxt() {
        makeDir();
        FileWriter fileWriter = null;
        String currentTimeYmd = CalendarUtil.getFormatYmd(System.currentTimeMillis());
        String currentTimeMdhm = CalendarUtil.getFormatMdhm(System.currentTimeMillis());
        String fileName = FILE_DIR + File.separator + "note_" + currentTimeYmd + ".txt";

        try {
            fileWriter = new FileWriter(fileName);
            NoteDao noteDao = NoteDaoImpl.getInstance();
            int[] count = noteDao.getCount();
            fileWriter.write(Resource.getString(R.string.note_export_txt_head_info, currentTimeMdhm, count[0], count[1]));
            fileWriter.write(NEW_LINE);
            fileWriter.write(NEW_LINE);
            fileWriter.write(NEW_LINE);
            List<Note> folders = noteDao.getFolders();

            for (Note folder : folders) {
                fileWriter.write(Resource.getString(R.string.note_export_txt_split_line));
                fileWriter.write(NEW_LINE);
                fileWriter.write(folder.getSubject());
                fileWriter.write(NEW_LINE);
                fileWriter.write(Resource.getString(R.string.note_export_txt_split_line));
                fileWriter.write(NEW_LINE);
                List<Note> children = noteDao.getChildren(folder);
                for (Note note : children) {
                    fileWriter.write(CalendarUtil.getFormatMdhm(note.getModifyTime()));
                    fileWriter.write(NEW_LINE);
                    fileWriter.write(note.getContent());
                    fileWriter.write(NEW_LINE);
                    fileWriter.write(NEW_LINE);
                    fileWriter.write(NEW_LINE);
                }
            }

            fileWriter.write(Resource.getString(R.string.note_export_txt_split_line));
            fileWriter.write(NEW_LINE);
            fileWriter.write(Resource.getString(R.string.app_name));
            fileWriter.write(NEW_LINE);
            fileWriter.write(Resource.getString(R.string.note_export_txt_split_line));
            fileWriter.write(NEW_LINE);
            List<Note> notes = noteDao.getAllNoParent();

            for (Note note : notes) {

                if (Note.TYPE_FOLDER == note.getType()) {
                    continue;
                }

                fileWriter.write(CalendarUtil.getFormatMdhm(note.getModifyTime()));
                fileWriter.write(NEW_LINE);
                fileWriter.write(note.getContent());
                fileWriter.write(NEW_LINE);
                fileWriter.write(NEW_LINE);
                fileWriter.write(NEW_LINE);
            }


        } catch (IOException e1) {
            e1.printStackTrace();
        } finally

        {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
