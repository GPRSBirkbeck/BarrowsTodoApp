# Barrows To-do App
This app is my work to create a simple TODO app for Barrows.

## Task Description
Build an android app that allows a user to create a simple to-do list in Kotlin.
Store all of the data locally.

## Instructions on how to use this project.
This app is a to-do app, built for Barrows.
To use this app: 
1. Install android studio and then wait for your build to complete. 
2. Next, attach a device or create an emulator, and click the green "Run" button. 
3. Once the app is up and running, click "Create a To-do item" in the bottom right. This will bring the To-do creation screen into the foreground.
4. Here you will need to add a title, body, select an icon, and select a due date before you can create a To-do item. 
5. Upon creating the To-do item, you will be brought back to the list screen. Here you can see all of your To-do items. 
6. Alternatively, click "Populate with test data" to see some example To-do items (this button is hidden if the list is not empty).

Each item on the list screen is clickable so you can update the item if needed.
When you create an item that is due today you will get an app notification saying it is due today.
When opening the app you will also get this notification, but only once per app session.

For the extended functionality, the user can either:
1. Search for a To-do item by title or body.
2. Sort the list a word entered into the search bar - either showing the entries that have the word most or least often in their titles and bodies.

## Some architectural notes
This is a single-activity architected app, using a navigation component to navigate between screens.