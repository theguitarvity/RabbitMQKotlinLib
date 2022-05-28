package br.com.rabbitmqkotlinlib.rabbitmq.examples

import br.com.rabbitmqkotlinlib.rabbitmq.Consumer

class ConsumerExample: Consumer() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val consumer: ConsumerExample = ConsumerExample()
            consumer.start("amqp://localhost:5672", "example.exchange, example.exchange2", "example.queue")
        }

    }

    override fun onMessage(message: String) {
        println(message)
    }
}