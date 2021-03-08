package app.com.firebaseauthentication

data class UserDetail(val firstName : String,val lastName :String, val email : String, val password : String,var photo_Url: String){
    constructor() : this("","","","","")
}
