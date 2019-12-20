package nl.codestar.interchange.producers

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.http.Protocol
import software.amazon.awssdk.http.SdkHttpConfigurationOption
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import software.amazon.awssdk.utils.AttributeMap
import java.net.URI
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class KinesisStringProducer (
    private val name: String,
    private val partition: String,
    region: Region,
    credentials: AwsCredentials
) {



    private val nettyClient: SdkAsyncHttpClient =
        NettyNioAsyncHttpClient
            .builder()
            .protocol(Protocol.HTTP1_1)
            .buildWithDefaults(
                AttributeMap
                    .builder()
                    .put(
                        SdkHttpConfigurationOption.TRUST_ALL_CERTIFICATES,
                        java.lang.Boolean.TRUE
                    )
                    .build()
            )

    private val builder = KinesisAsyncClient
        .builder()
        .region(region)
        .endpointOverride(URI.create("http://localhost:4567"))
        .credentialsProvider { credentials }
        .httpClient(nettyClient)

    private val client = builder.build()

    private fun createStream() {
        println("Creating a stream")
        val request = CreateStreamRequest
            .builder()
            .streamName(name)
            .shardCount(1)
            .build()

        val response = client.createStream(request).get(10, TimeUnit.SECONDS)

        println(response)
    }

    private fun createRecord(json: String) {
        println("Creating a record")
        val request = PutRecordRequest
            .builder()
            .streamName(name)
            .partitionKey(partition)
            .data(SdkBytes.fromString(json, Charset.defaultCharset()))
            .build()

        val response = client.putRecord(request).get(10, TimeUnit.SECONDS)

        println("Done create record")
        println(response)
    }

    fun publish(json: String) {
        createRecord(json)
    }

    init {
        System.setProperty("aws.cborEnabled", "false")
        createStream()
    }
}
