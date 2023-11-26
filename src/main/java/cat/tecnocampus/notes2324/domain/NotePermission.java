package cat.tecnocampus.notes2324.domain;

import jakarta.persistence.*;

@Entity
public class NotePermission {
    @EmbeddedId
    private NotePermissionId id;
    private boolean canView;
    private boolean canEdit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noteId")
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User allowed;

    public NotePermission(User allowed, Note note) {
        this.id = new NotePermissionId(note.getId(), allowed.getId());
        this.note = note;
        this.allowed = allowed;
    }

    public NotePermission() {

    }

    public NotePermissionId getId() {
        return id;
    }

    public void setId(NotePermissionId id) {
        this.id = id;
    }

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public User getAllowed() {
        return allowed;
    }

    public void setAllowed(User owner) {
        this.allowed = owner;
    }
}