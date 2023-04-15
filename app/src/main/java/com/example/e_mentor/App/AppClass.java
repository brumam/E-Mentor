package com.example.e_mentor.App;

import com.example.e_mentor.Helpers.User;

public class AppClass {
    // Public User Helper class that stores the user information after login
    public static class Session {
        public static User user;

        // Logs out the user by setting the user object to null
        public static void logout() {
            user = null;
        }

        // Checks if the user is a student
        public static boolean isStudent() {
            if (user == null) {
                return false;
            }
            return user.isStudent();
        }

        // Checks if the user is a teacher
        public static boolean isTeacher() {
            if (user == null) {
                return false;
            }
            return user.isTeacher();
        }
    }
}
