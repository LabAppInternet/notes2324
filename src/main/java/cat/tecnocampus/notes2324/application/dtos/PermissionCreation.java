package cat.tecnocampus.notes2324.application.dtos;

public record PermissionCreation(long allowedId, long noteId, boolean canView, boolean canEdit) {
}
