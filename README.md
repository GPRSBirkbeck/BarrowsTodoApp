# Barrows To-do App
This app is my work to create a simple TODO app for Barrows.

## Task Description
Build an android app that allows a user to create a simple to-do list in Kotlin.
Store all of the data locally.

## Instructions on how to use this project.
TODO: Populate this when "Basics" are complete, and again when "Extended functionality" is complete.

## Some architectural notes
This is a single-activity architected app, using a navigation component to navigate between screens.

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
2. Users want to sort their to-dos in ascending or descending order of the number of occurrences of a specific word. Implement this sorting function.
3. users want to share their to-do entries with others via QR codes. Implement functionality that generates a QR code (or other) from the contents of a to-do entry which when scanned by another user allows them to import this entry into their to-do list.
4. Both applications involved in this transfer should display some kind of integrity check (like a hash or checkSum) so that the two users can be sure that the data was transferred without errors.

## Steps
The first todo, is to actually break the task down into steps.
"Basics" steps:
1. Creating to-do items (details screen)
   1. Create the database, DAO and model. Check!
   2. Then, setup my repository and module to allow repository and DB injections. Check!
   3. Next, add a "Create Todo" button. Check!
   4. Create a screen that has inputs for text inputs, deadline selector, and icon selector - the date should use a date picker - and not some input field. Check!
   5. Add a "Create" button that saves the item when clicked and navigates us back to the list. Check!
2. Displaying to-do items (home screen)
   1. Create composable for to-do item. Check!
   2. load to-do items from DB. Check! 
   3. make icon red for a to-do item has passed its deadline (play around and see what I like the look of most) Check!
   4. add a days until due subtitle. Check!
3. Update to-do items
   1. Make the to-do items clickable.
   2. Upon clicking, reuse the creation screen, and allow the user to update the fields
   3. Add a save button to the bottom, which only becomes enabled once the user has made a change
4. Delete to-do items
   1. Add a delete button to the reused creation screen - check!
   2. Add swipe to delete functionality - check out the repo I made with swipe to delete functionality - check!
5. Push notifications:
   1. Decide if I want to implement this. If yes, make it easy to test in the dataset I'm giving to the tester.
   2. setup service for this - I'll add more details here if I decide to proceed.
6. If I haven't and I've rushed into building things - add some tests please - even if it's just to show I can write tests ofc.
7. Make "Basics" production ready:
   1. update app icon. Check!
   2. review code:
      1. when I worked through the steps above, did I opt for speed over best practice?
      2. How do I feel about the current state of my dependency injection?
      3. Have a think about my "state" setup - I think this can be improved upon
      4. Review my packages - could these be simplified?
      5. Review threading
      6. How's my unidirectional data flow?
      7. Check out the AppNavHost one more time.
      8. Check out my use of todo vs. To-do vs Todo naming conventions - I imagine there will be a case or two where these aren't aligned.
      9. Think about renaming it all to task - not Todo - as this might be simpler - have a think.
   3. Think about the domain layer one more time
   4. review gradle - do I have any unneeded dependencies?
   5. Add instructions on how to use this app into this README (also check for punctuation - I don't care if I end lines with dots, just make it consistent). 
   6. Add a datastore of test/dummy data for the assessor to use - I think for this, maybe just have a button they can click to populate the list. Hide the button if the list is not empty.
   7. Reread the instructions one more time, did I do what they asked for?
   8. Check threading one more time
   9. Make it clear in commit history now that basics are complete.

"Extended" steps:
1. Search through all the titles of their to-dos
   1. Add Search bar
   2. Implement search: VM -> Repo -> DB
   3. Test edge cases and double check threading.
2. Sort users to-dos in ascending or descending order of the number of occurrences of a specific word.
   1. Add Icon to left of search bar that toggles between ascending or descending
   2. Add nice list animation when they change between ascending and descending
   3. Figure out which user journey exactly triggers this - it's not clear at this time.
3. Share entries via QR code: 
   1. Create QR code creation service
   2. Add icon/button to trigger sharing.
   3. set up so that camera that scans this knows to use BarrowsTodoList app - decide if I want to trigger scanning from within the app - if not, give the user a prompt on camera opening etc. If I do in app, ofc remember to ask for permissions - just putting this here in case I forget - I won't.
   4. when scanned - refresh and add to the list - I think I want to add a subtitle that tells the user it was scanned from a friends todo list.
4. Scan Integrity check (like a hash or checkSum) so that the two users can be sure that the data was transferred without errors.
   1. think about implementation later, I'm happy with the steps so far.
5. Make "Extended Functionality" production ready
   1. Details TBC - similar to the steps in 'Make "Basics" production ready'

"Submission" steps:
1. Review my readme one more time - instructions on how to run the project need to be great.
2. ensure the repository is public - it isn't yet, so make it public.