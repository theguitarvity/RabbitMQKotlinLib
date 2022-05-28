package br.com.rabbitmqkotlinlib.rabbitmq.examples

import br.com.rabbitmqkotlinlib.rabbitmq.Producer

class ProducerExample: Producer() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val message: String = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}"
            val producer: ProducerExample = ProducerExample()
            producer.publish("amqp://localhost:5672", "example.exchange", "", message)
        }
    }
}