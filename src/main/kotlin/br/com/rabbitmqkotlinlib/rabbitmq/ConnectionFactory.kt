package br.com.rabbitmqkotlinlib.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

class ConnectionFactory {
    companion object {
        fun createConnection(rabbitmqUri: String): Connection {
            val factory: com.rabbitmq.client.ConnectionFactory = ConnectionFactory()
            lateinit var connection: Connection
            try {
                factory.setUri(rabbitmqUri)
                connection = factory.newConnection()
            }
            catch (err: Exception){
                println("Error on connect to rabbitmq")
            }

            return connection
        }
        fun createChannel(rabbitmqUri: String): Channel {
            lateinit var channel: Channel
            try {
                channel = createConnection(rabbitmqUri).createChannel();
            }
            catch (err: Exception) {
                println("Error on creating channel")
            }

            return channel
        }
    }
}