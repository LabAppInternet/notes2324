package cat.tecnocampus.notes2324;

import cat.tecnocampus.notes2324.application.NotesService;
import cat.tecnocampus.notes2324.domain.Note;
import cat.tecnocampus.notes2324.domain.NotePermission;
import cat.tecnocampus.notes2324.domain.NotePermissionId;
import cat.tecnocampus.notes2324.domain.User;
import cat.tecnocampus.notes2324.persistence.NotePermissionRepository;
import cat.tecnocampus.notes2324.persistence.NoteRepository;
import cat.tecnocampus.notes2324.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// TODO 4.0: See them fail and than make them pass completing the exercise
@SpringBootTest
@AutoConfigureMockMvc
public class TODO4tests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    NotePermissionRepository notePermissionRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void revokePermissionDelete() throws Exception {
        String permission = """
                {
                  "allowedId": 4,
                  "noteId": 4,
                  "canEdit": false,
                  "canView": false
                }""";
        mockMvc.perform(delete("/users/2/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(permission))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        assertFalse(notePermissionRepository.existsById(new NotePermissionId(4L, 4L)));

        //leave the database as it was
        User allowedUser = userRepository.findById(4L).get();
        Note note = noteRepository.findById(4L).get();
        NotePermission notePermission = new NotePermission(allowedUser, note);
        notePermission.setCanEdit(true);
        notePermission.setCanView(true);
        notePermissionRepository.save(notePermission);

    }
    @Test
    void revokePermissionUpdate() throws Exception {
        String permission = """
                {
                  "allowedId": 4,
                  "noteId": 5,
                  "canEdit": false,
                  "canView": true
                }""";
        mockMvc.perform(delete("/users/2/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(permission))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        NotePermission notePermission = notePermissionRepository.findById(new NotePermissionId(5L, 4L)).orElseThrow();
        assertTrue(notePermission.isCanView() && !notePermission.isCanEdit());

        //leave the database as it was
        notePermission.setCanEdit(true);
        notePermissionRepository.save(notePermission);
    }

    @Test
    void revokePermissionUserNotOwner() throws Exception {
        String permission = """
                {
                  "allowedId": 4,
                  "noteId": 5,
                  "canEdit": false,
                  "canView": true
                }""";
        mockMvc.perform(delete("/users/1/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(permission))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User with id: 1 does not own note with id: 5"));
    }
}
