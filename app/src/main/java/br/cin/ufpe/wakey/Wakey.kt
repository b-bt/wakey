package br.cin.ufpe.wakey

class Wakey(latitude: Double, longitude: Double, radius: Float, name: String){

    val latitude = latitude
    val longitude = longitude
    val radius = radius
    val name = name
    var active = true

    fun activate(){
        active = true
    }

    fun deactivate(){
       active = false
    }

}
