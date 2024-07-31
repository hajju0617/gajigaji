// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyCuV5CBfZqJJVk_dTeOvYTjc8fKj4AaLbM",
    authDomain: "project2nd-216ef.firebaseapp.com",
    projectId: "project2nd-216ef",
    storageBucket: "project2nd-216ef.appspot.com",
    messagingSenderId: "400263351210",
    appId: "1:400263351210:web:cb724a398e695d3dd279d3",
    measurementId: "G-6PF8YCWEBL"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);