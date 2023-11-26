package cat.tecnocampus.notes2324.application.dtos;

public record PermissionDTO(long ownerId, String ownerName, long noteId, String noteTitle, boolean canView, boolean canEdit) {
}
