package br.com.rabbitmqkotlinlib.rabbitmq

class RabbitMQConsumerOptions {
    private var preFetch: Int = 100
    private var routingKey: String = ""

    fun getPreFetch(): Int {
        return preFetch
    }

    fun setPreFetch(preFetch: Int) {
        this.preFetch = preFetch
    }

    fun getRoutingKey(): String {
        return routingKey
    }

    fun setRoutingKey(routingKey: String) {
        this.routingKey = routingKey
    }

}