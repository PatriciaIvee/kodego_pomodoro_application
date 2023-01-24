package ph.kodego.leones.patricia.ivee.pomodoroapplication.model

import java.sql.Time

class Task (var taskName:String = "Unknown", var status:String = "Unknown") {

    var id: Int = 0
    var taskDescription : String = ""
    var taskPriority : String = ""
    var focusTime : Int = 0
    var shortBreak : Int = 0
    var longBreak : Int = 0
    var timeInterval : Int = 0

    //    constructor for default values (firstname, lastname, profile picture)
    constructor(): this("","")


}