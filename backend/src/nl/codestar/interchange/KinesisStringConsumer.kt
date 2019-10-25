package nl.codestar.interchange

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.http.Protocol
import software.amazon.awssdk.http.SdkHttpConfigurationOption
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.RegisterStreamConsumerRequest
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest
import software.amazon.awssdk.utils.AttributeMap
import software.amazon.kinesis.common.KinesisClientUtil
import java.net.URI
import java.util.concurrent.TimeUnit

class KinesisStringConsumer {
  private val streamName = "string"
  private val consumerName = "string-consumer-1"
  private val credentials = AwsBasicCredentials.create("test-id", "test-key")
  private val region = Region.of("eu-west-1")

  val nettyClient: SdkAsyncHttpClient =
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
    .region(this.region)
    .endpointOverride(URI.create("http://localhost:4567"))
    .credentialsProvider { credentials }
    .httpClient(nettyClient)

  private val client = builder.build()

  fun run() {
    val request = RegisterStreamConsumerRequest
      .builder()
      .consumerName(consumerName)
      .streamARN(streamName)
      .build()


    val response = client.registerStreamConsumer(request).get(10, TimeUnit.SECONDS)

  }


}
