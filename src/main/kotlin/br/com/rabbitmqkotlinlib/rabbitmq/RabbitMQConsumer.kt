package br.com.rabbitmqkotlinlib.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

abstract class RabbitMQConsumer {
    private lateinit var connection: Connection
    private lateinit var channel: Channel
    private lateinit var rabbitmqUri: String
    private lateinit var consumerOptions: RabbitMQConsumerOptions
    private lateinit var exchange: String
    private lateinit var queue: String
    private var connected: Boolean = false


    private fun startConsumer(){
        if(connection == null){
            connection = connect(rabbitmqUri)
        }

        channel = connection.createChannel()
        channel.queueDeclare(queue, true, false, false, null)

        declareAndBindExchange(exchange, queue)

    }

    private fun declareAndBindExchange(exchange: String, queue: String) {

    }

    private fun connect(rabbitmqUri: String): Connection {
        val factory: com.rabbitmq.client.ConnectionFactory = ConnectionFactory()
        factory.setUri(rabbitmqUri)

        return factory.newConnection()
    }

}