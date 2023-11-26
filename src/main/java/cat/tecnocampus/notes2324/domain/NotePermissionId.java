package cat.tecnocampus.notes2324.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class NotePermissionId implements Serializable {
    private long noteId;
    private long userId;

    public NotePermissionId() {
    }
    public NotePermissionId(long noteId, long userId) {
        this.noteId = noteId;
        this.userId = userId;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}