package cat.tecnocampus.notes2324.application.dtos;

// TODO 4.1 this record is used to create a permission. You really don't need to change anything here
public record PermissionCreation(long allowedId, long noteId, boolean canView, boolean canEdit) {
}
