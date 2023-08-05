package com.example.assignmenttest.model;

public class UserModel {


        public int id;
        public String userName;
        public static String password;
        public String email;
        public String date_registered;
        public String date_updated;

        public UserModel() {
        }

        public UserModel(String userName, String email, String date_registered) {
                this.userName = userName;
                this.email = email;
                this.date_registered = date_registered;
        }
}
