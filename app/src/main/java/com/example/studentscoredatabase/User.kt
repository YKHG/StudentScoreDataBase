package com.example.studentscoredatabase

class User (var id: Int, val subject:String , val score: String) {
    override fun toString(): String { // return the record detail
        return "$id: $subject($score)"
    }
}