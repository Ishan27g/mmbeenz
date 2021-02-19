package com.mmb

import java.io.File
import kotlin.math.roundToInt

class User (val username: String, var password: String, var img : File){
    private var rating: Float = 0F//2.51f
    var tempRating = mutableListOf<Float>()

    private fun updateRate(){
        this.rating = 0F
        for (rating in tempRating){
            this.rating += rating
        }
        this.rating = this.rating / tempRating.size
    }
    fun rate(newRating: Int){
        tempRating.add(newRating.toFloat())
        updateRate()
    }
    fun getRating(): Int{
        var ret = rating.roundToInt()
        return ret
    }
}

open class UserList(){
    private var list = hashMapOf<String, User>()
    fun addUser(username: String, password: String, img: File){
        var newUser = User(username, password, img)
        list[username] = newUser
    }
    fun rateUser(username: String, rating: Int): Boolean{
        var user = list[username]
        if (user != null) {
            list.remove(username)
            user.rate(rating)
            list[username] = user
            return true
        }
        return false
    }
    fun getUserRating(username: String) : Int? {
        return list[username]?.getRating()
    }
    fun getAllUsers() : List<User>?{
        return list.values.toList()
    }
    fun getLoginInfo() : Map<String, String> {
        var rsp = mutableMapOf<String, String >()
        var users = getAllUsers()
        users?.iterator()?.forEach { user ->
            rsp[user.username] = user.password
        }
        return rsp.toMap()
    }

    private val names= arrayListOf("Jeff","Britta","Pierce","Troy","Abed","Annie", "Shirley")
    fun setSampleData(){
        fun getRandomRating(): Int{
            return (1..5).shuffled().first()
        }
        for (name in names){
            addUser(name, "password", File("") )
            var i=0
            var random = getRandomRating() * getRandomRating()
            while (i++ < 10 + random){
               rateUser(name, getRandomRating())
            }
        }

    }

}