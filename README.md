# Barrows To-do App
This app is my work to create a simple TODO app for barrows.

## Task Description
Build an android app that allows a user to create a simple to-do list in Kotlin.
Store all of the data locally.

## Task Specifications
Basics:
1. A user must be able to view, add, edit and delete to-do entries.
2. To-do entries, which:
    1. Have a title, which is a text.
   2. a body, which is text.
   3. an icon - not uploadable, but one from a set available in the app.
   4. a date that the entry is due. This date can only be in the future.
3. The application should make it clear to a user when an entry has passed its due date, for example by turning the background of the entry red.
I may implement push notifications for expiring entries if I want, but it's not a requirement.

Extended functionality:
1. Users want to search through all the titles of their to-dos. Implement this search functionality.
2. Users want to sort their to-dos in ascending or descending order of the number of occurences of a specific word. Implement this sorting function.
3. users want to share their to-do entries with others via QR codes. Implement functionality that generates a QR code (or other) from the contents of a to-do entry which when scanned by another user allows them to import this entry into their to-do list.
4. Both applications involved in this transfer should display some kind of integrity check (like a hash or checkSum) so that the two users can be sure that the data was transferred without errors.

Clearly mark in commit history once the basics are done.


## Implementation:
1. A home screen which is the list of to-do entries.
2. A details screen, which takes in an item, but only updates upon clicking a save button.
3. the date should use a date picker - and not some input.

## TODO
- Add use of RoomDB and DAOs.
- Check out latest compose template to see how they handle MyApp instead of just MainActivity.
- Add instructions on how to use this app into this readme.
- My submission should also contain a datastore of test/dummy data for the assessor to use.

## Tech stack I used
- Compose

## Some notes
- this template follows android architecture as outlined in [here](https://developer.android.com/topic/architecture#common-principles)
- It follows unidirectional data flow - with ui elements triggering events, state models (state data classes), and viewModels that expose data to the UI, and handle logic
- the UI layer is stored in the ui package.
- the data layer is stored in the data package
- as recommended, I'm using a single activity application, with Navigation compose to navigate between screens - the code for this is kept in my navigation package.
- the viewModels are instantiated in the AppNavHost, following the recommended practice of doing it there from this information on hilt working with viewModels in compose: [here](https://developer.android.com/develop/ui/compose/libraries#hilt)

# Submission Guidelines
- Code must be submitted using a public git repository in Github, with a full commit history.
- Do not do the whole project in one go and commit it all at once, you use of git is an important part of this assessment.
- Instructions to run the project are needed in this readme.