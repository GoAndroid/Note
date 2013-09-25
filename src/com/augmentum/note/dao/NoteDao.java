package com.augmentum.note.dao;

import com.augmentum.note.model.Note;

import java.util.List;

public interface NoteDao {

    /**
     * Insert a <code>Note</code> object to database, a note will be insert with a type, parent_id, create_time,
     * if is a note_type insert with color, modifyTime, Content,
     * if is a folder_type insert with subject.
     *
     * @param note which <code>note</code> object will be insert
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(Note note);

    /**
     * Update a <code>Note</code> object to database,
     * if is a note_type with parent_id, color, content, modify_time
     * if is a folder_type with subject
     *
     * @param note which <code>Note</code> object will be delete Id can't be null
     */
    public void update(Note note);

    /**
     * Delete a <code>Note</code> object from database by id,
     * if note is a folder_type delete with children.
     *
     * @param note which <code>Note</code> be delete Id can't be null
     */
    public void delete(Note note);

    /**
     * Get All <code>Note</code> object have no parent.
     *
     * @return a list of <code>Note</code> object
     */
    public List<Note> getAllNoParent();

    /**
     * Get all <code>Note</code> object what are children of parent.
     * @param parent a <code>Note</code>object type is folder
     * @return a list of <code>Note</code> object type is note
     */
    public List<Note> getChildren(Note parent);

    /**
     * Get all <code>Note</code> object which type is folder
     * @return a list of folder type <code>Note</code> Object
     */
    public List<Note> getFolders();

}
