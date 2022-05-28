package br.com.rabbitmqkotlinlib.rabbitmq

import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.jetbrains.annotations.NotNull

abstract class Producer {
    lateinit var connection: Connection
    lateinit var chanel: Channel

    public fun publish(
        @NotNull uri: String,
        @NotNull exchange: String,
        @NotNull routingkey: String,
        basicProperties: BasicProperties?,
        message: String
    ){
        try {
            connection = connect(uri)
            chanel = connection.createChannel()
            chanel.basicPublish(exchange, routingkey, basicProperties, message.toByteArray())

            println("pushing message on  $exchange with routing key $routingkey and message $message")

        }catch (exception: Exception){
            println("Error on publish message: $message")
            throw RuntimeException(exception)
        } finally {
            stop()
        }

    }
    public fun publish(
        @NotNull uri: String,
        @NotNull exchange: String,
        @NotNull routingkey: String,
        message: String
    ){
        publish(uri, exchange, routingkey, null, message)
    }
    private fun connect(rabbitmqUri: String): Connection {
        val factory: com.rabbitmq.client.ConnectionFactory = ConnectionFactory()
        factory.setUri(rabbitmqUri)

        return factory.newConnection()
    }
    public fun stop(){
        try {
            connection.close()
        }catch (exception: Exception){
            println("error on stop rabbitmq consumer")
        }
    }
}