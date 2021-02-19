package com.mmb

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.server.engine.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.*
import java.io.File
import java.security.MessageDigest

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {

    var allUser= UserList()

    /*Sample data for testing only */
    allUser.setSampleData()
    /*var testUsers = allUser.getAllUsers()
     testUsers?.iterator()?.forEach { user ->
        println("${user.username} : " + user.getRating())
    }*/

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }
    install(ShutDownUrl.ApplicationCallFeature) {
        shutDownUrl = "/shutdown"
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }
    install(Authentication) {
        basic("basicAuth") {
            realm = "Ktor Server"
            validate {
                var users = allUser.getLoginInfo()
                var found = false
                for (user in users){
                    println(user.key)
                    println(user.value)
                    if (user.key == it.name && user.value == it.password){
                        found = true
                    }
                }
                if (found) UserIdPrincipal(it.name) else null
            }
        }
    }
    install(ContentNegotiation) {
        gson {
        }
    }


    routing {
        static("image"){
            files("resources/profiles")
        }
        post("/register") {
            val userName: String? = call.request.queryParameters["username"]
            val password: String? = call.request.queryParameters["password"]
            if (userName != null && password != null) {
                val mp = call.receiveMultipart()
                var img = File("")
                var res = false
                mp.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val path = System.getProperty("user.dir")
                        img = File("$path/resources/profiles/$userName.png")
                        part.streamProvider().use { its ->
                            // copy the stream to the file with buffering
                            img.outputStream().buffered().use {
                                its.copyTo(it)
                                res = true
                            }
                        }
                    }
                    part.dispose()
                }
                if (res) {
                    allUser.addUser(userName, password, img)
                    call.respond(HttpStatusCode.Created)
                } else call.respond(HttpStatusCode.BadRequest)
            } else call.respond(HttpStatusCode.BadRequest)
        }
        authenticate("basicAuth") {
            get("/user") {
                val userName: String? = call.request.queryParameters["name"]
                if (userName != null) {
                    var userRating = allUser.getUserRating(userName)
                    call.respond(HttpStatusCode.Accepted, mapOf(userName.toString() to userRating.toString()))
                } else {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "user not found"))
                }
            }
            get("/allUsers") { //login, homepage
                var users = allUser.getAllUsers()
                var rsp = mutableListOf<UserData>()
                users?.iterator()?.forEach { user ->
                    rsp.add(fillUserData(user))
                }
                call.respond(rsp)
            }
            post("/user"){
                val userName: String? = call.request.queryParameters["name"]
                val rating: String? = call.request.queryParameters["rating"]
                println(userName)
                println(rating)
                if (userName != null && rating != null) {
                    var check = allUser.rateUser(userName, rating.toInt())
                    if (check) call.respond(mapOf(userName.toString() to
                            allUser.getUserRating(userName).toString())) else
                        call.respond(mapOf("error" to "user not rated"))
                } else call.respond(mapOf("error" to "user/rating param not found"))
            }


        }
    }
}
data class UserData(val username: String, val rating : Int)
fun fillUserData(user: User) : UserData{
    return UserData(user.username, user.getRating())
}

