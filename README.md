# Student Management System

## Overview

This Android application is a Student Management System developed using Java and Android Studio. The app utilizes Firebase for authentication and provides functionality to manage Colleges, Courses, Sections, and Students with full CRUD (Create, Read, Update, Delete) operations.

## Features

- **User Authentication**: Login and manage user sessions using Firebase Authentication.
- **College Management**: Add, update, delete, and view colleges.
- **Course Management**: Add, update, delete, and view courses associated with colleges.
- **Section Management**: Manage sections within courses.
- **Student Management**: Add, update, delete, and view students associated with sections.

## Configure Firebase

### Download `google-services.json`

To connect your app with Firebase services, you need to obtain a `google-services.json` file. This file contains the necessary configuration details for your Firebase project.

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Select your project.
3. Navigate to `Project Settings` (gear icon) > `General`.
4. In the `Your apps` section, download the `google-services.json` file.

### Add `google-services.json` to Your Project

1. Place the downloaded `google-services.json` file into the `app/` directory of your project.

    ```plaintext
    YourProject/
    └── app/
        └── google-services.json
    ```

This file is essential for integrating Firebase services into your app.
