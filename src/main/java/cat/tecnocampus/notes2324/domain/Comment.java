package cat.tecnocampus.notes2324.domain;

import cat.tecnocampus.notes2324.configuration.TsidUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
    @Id
    private long id;
    private String title;
    private String body;
    @ManyToOne
    private Note note;

    public Comment() {
        id = TsidUtils.getTsidFactory(1).generate().toLong();
    }

    public Comment(String title, String body) {
        id = TsidUtils.getTsidFactory(1).generate().toLong();
        this.title = title;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
