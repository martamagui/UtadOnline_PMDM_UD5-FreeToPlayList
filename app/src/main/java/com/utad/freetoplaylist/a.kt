package com.utad.freetoplaylist

import android.util.Log

interface RepartidorRepository {
    fun entregarPizza(direccion: String): Pizza
}

class RepartidorRepositoryJuanImpl(): RepartidorRepository{
    override fun entregarPizza(direccion: String): Pizza {
        Log.i("Juan", "Juan conduce a la dirección en coche")
        Log.i("Juan", "Juan entrega la pizza")
        return Pizza()
    }
}
class RepartidorRepositoryAlejandroImpl(): RepartidorRepository{
    override fun entregarPizza(direccion: String): Pizza {
        Log.i("Juan", "Alejandro conduce a la dirección en moto")
        Log.i("Juan", "Alejandro entrega la pizza")
        return Pizza()
    }
}


class Pizza(){
}