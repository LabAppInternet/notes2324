# Exam course 2023-2024 

## Before you begin
* Read this file carefully
* BEFORE **touching** the code, run the application and see it working on your computer. You may also want to run the tests in *Notes2324ApplicationTests*
to see them pass. The tests in the other test files should fail until you implement the corresponding exercise.
* You have example calls in the file "resources/notes.http" but it might be easier to use the **swagger** documentation at http://localhost:8080/swagger-ui.html
* You have the h2-console enabled at http://localhost:8080/h2-console. The database is in memory, so you can see the data in the console. 
* When attempting to answer the questions, modify the code in small steps and try the application (run it) after every step. In this way, it is easier to track possible errors
* A code that doesn't compile or run will be marked zero points
* Read the questions and the TODOs
* All the questions are independent and can be answered in any order. So, if you get stuck in a question go ahead and attempt to answer another one.
* In the code you'll see **TODO**s where you need to insert new code. TODOs explain what you need to do and may contain some clues. Please,
  don't delete the TODOs from the code. TODOs are numbered according to the question number. When a question has more than one TODO they are
  numbered TODO X.1, TODO X.2 and so on, where X is the question number. There are few TODOs that don't need any code, they are there to explain code relevant to the question (and its answer)

#### TODOs in Intellij
In Intellij you can see all the TODOs in the TODO tool window. You can open it using the menu View -> Tool Windows -> TODO.
It is a good idea to use this tool window to keep track of the TODOs you have to do. You can also navigate to the TODOs in the from this window.

## Taking and Sharing Notes
In this application, **users** are able to create and edit **notes** as well as giving permission to view and/or edit notes to other users. 
Notes have a title, a content, a creation date, and also a list of tags. **Tags** are identified by their name (string) that can be used to classify notes.

The relationship between users (owners) and notes is implemented as a @OneToMany relationship. This means that notes have a reference to their owner. See that the
Note class implements the method isOwner(userId) that returns true if the given user is the owner of the note. You may want to use this method in Question 4.

A **NotePermission** is a class that represents the permission that a user has over a note. A NotePermission has a user (the user that has the permission), 
a note (the note that the user has permission to see and/or edit) and two booleans representing the permissions (view and/or edit). See that 
this class is identified by the user and the note with an embedded id.

The **data.sql** file contains some data to test the application. If you change it, some tests may fail. There are users, notes, tags and permissions.

You also have test suites that you can run to check whether your code is correct. Tests can help you to see if your code is correct, but passing the tests doesn't mean 
that your code is correct. 

## Questions 
Each question is independent of the others. You can answer them in any order. Two points are given for each question.

### Question 1: Validation
When notes are created or updated, the title must begin with a capital letter and contain only letters, spaces and any of the following characters: À-ÿ.,;:_'- 
Where À-ÿ are the accented characters in Spanish and Catalan (À-ÿ represents the set of all accented characters, from À to ÿ).
In the title no numbers are allowed. 

The content must begin with a capital letter and contain any of the letters as specified in the title. In addition, the content can also have numbers.

Both title and content must be between 5 and 100 characters long.

You may want to copy an entire file from the Tinder or your own project to implement the validation.

### Question 2: Query top owners
We want an ordered list of the users that have created more notes during the last 30 days. The list must be ordered by the number of notes created in descending order.

### Question 3: Owners can add comments to their notes (and only to their notes). Also, can see their comments and the comments of other notes they have permission to view
A comment of a note has a title and a body. A note can have many comments and a comment belongs to a note. You should implement the relationship as a @ManyToOne.
To create a comment, the call has the following format:
```POST /users/{userId}/notes/{noteId}/comments``` and the body of the call is the comment to create. The comment must have a title and a body.
To list the comments of a note, the call has the following format:
```GET /users/{userId}/notes/{noteId}/comments```
Don't forget to use a DTO to return the comments. The DTO must have the id, title and body of the comment.

### Question 4: Complete the revokePermission method
A note owner may revoke a permission to a user. In fact, it is an operation that deletes a NotePermission when both edit and view permissions are revoked, and
it updates the permissions when only one of them is revoked.
You must finish the method revokePermission in NoteRestController. The method receives the id of the note's owner and the permission to update/delete

### Question 5: testing
Test the entry GET /users/{id}  You need to write two tests. One for the happy path where you provide with an existing user id (1). The
call with id 1 should return isOk HTTP code (200) and
should have "Pere" as name, "pere@tecnocampus.cat" as email and should have 3 notes (you don't need to check which ones). The second test
with a non-existing user with id 1000. It should return isBadRequest HTTP code (400) and the response body should be "User with id: 100 not found"