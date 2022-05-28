package br.com.rabbitmqkotlinlib.rabbitmq

import com.rabbitmq.client.*
import com.rabbitmq.client.ConnectionFactory
import java.io.IOException
import java.util.concurrent.TimeoutException

abstract class Consumer {
    private lateinit var connection: Connection
    private lateinit var channel: Channel
    private lateinit var rabbitmqUri: String
    private lateinit var consumerOptions: RabbitMQConsumerOptions
    private lateinit var exchanges: String
    private lateinit var queue: String
    private var connected: Boolean = false



    public fun start(uri: String, exchanges: String, queue: String, consumerOptions: RabbitMQConsumerOptions){
        this.rabbitmqUri = uri
        this.exchanges = exchanges
        this.queue = queue
        this.consumerOptions = consumerOptions

        try {
            startConsumer()

        }catch (exception: Exception){
            println("error on starting rabbitmq consumer")
        }
    }

    public fun start(uri: String, exchanges: String, queue: String, routingKey: String){
        val options: RabbitMQConsumerOptions = RabbitMQConsumerOptions()

        options.setRoutingKey(routingKey)
        start(uri, exchanges, queue, options)
    }

    public fun start(uri: String, exchanges: String, queue: String){
        start(uri, exchanges, queue, RabbitMQConsumerOptions())
    }
    public fun stop(){
        try {
            connected = false
            channel.close()
            connection.close()
        }catch (exception: Exception){
            println("error on stop rabbitmq consumer")
        }
    }
    private fun startConsumer(){

        connection = connect(rabbitmqUri)
        channel = connection.createChannel()
        channel.queueDeclare(queue, true, false, false, null)

        declareAndBindExchange(exchanges, queue)

        channel.basicQos(this.consumerOptions.getPreFetch())
        channel.basicConsume(queue, false, object: DefaultConsumer(channel) {
            @Throws(IOException::class)
            override fun handleDelivery(
                consumerTag: String,
                envelope: Envelope,
                properties: AMQP.BasicProperties,
                body: ByteArray
            ) {
                val message: String = String(body)

                onMessage(message)

                if(!channel.isOpen){
                    println("channel is closed")
                    return
                }

                channel.basicAck(envelope.deliveryTag, false)

            }
        })

        channel.addShutdownListener { reconnect() }

        connected = true

    }

    private fun reconnect() {
        try {
            if(channel.isOpen){
                channel.close()
            }
        } catch (timoutException: TimeoutException){
            println("timout")
        } catch (ioException: IOException){
            println("io exception")
        }

        try {
            if(connection.isOpen){
                connection.close()
            }
        } catch (ioException: IOException){
            println("io exception")
        }

        restart()


    }

    private fun restart() {
        println("restarting consumer")

        while(!connected){
            println("connecting to rabbitmq")

            try{
                startConsumer()

            }catch (exception: Exception){
                println("there is an error on restarting consumer")
                waitForRetry()
            }
        }
    }

    private fun waitForRetry() {
        try {
            Thread.sleep(10000)
        }catch (exception: InterruptedException){
            println("the wait was interrupted")
        }
    }

    public abstract fun onMessage(message: String)


    private fun declareAndBindExchange(exchanges: String, queue: String) {
        val exchangeNames = exchanges.split(",")

        for (exchange in exchangeNames){
            channel.exchangeDeclare(exchange, "direct", true)
            channel.queueBind(queue, exchange, consumerOptions.getRoutingKey())
        }
    }

    private fun connect(rabbitmqUri: String): Connection {
        val factory: com.rabbitmq.client.ConnectionFactory = ConnectionFactory()
        factory.setUri(rabbitmqUri)

        return factory.newConnection()
    }

}