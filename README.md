The above program will help in uploading images and text to firebase storage under Spark plan(free)
The image will be inserted from your cell phone
If you don't have firebase account then create one by following the below steps:
1. https://firebase.google.com/ go on this link and register with details
2. Then in top right corner select for console mode.
3. Add a new project (in our case Android project)
4. Then go to DEVELOP section on left tab -> Select Database -> Edit the Rules in that section and paste as given below
    {
        "rules": {
        ".read": true,
        ".write": true
      }
    }
    
5. Then go to Storage -> Edit the rules 
    rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true;           //replace this part by putting auth != null for specified access
    }
  }
}


Then configure the project in android studio by simply selection Tools -> Firebase -> Storage
And then follow the steps.

You can also configure and use different tools like MLkit, Function, Realtime Database and many more services provided by firebase in a similar way
Don't forget to update all the dependencies as and when new one's are available.
