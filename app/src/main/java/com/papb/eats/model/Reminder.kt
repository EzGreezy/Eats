package com.papb.eats.model

class Reminder(time: String, title: String) {
    var title: String? = ""
    var time: String = ""
    var completed: Boolean = false
    var id: String = ""
    var timeMillis: String = ""
}