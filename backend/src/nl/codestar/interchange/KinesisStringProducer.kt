package nl.codestar.interchange

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.CreateStreamRequest
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest
import software.amazon.kinesis.common.KinesisClientUtil
import java.net.URI
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class KinesisStringProducer {
  private val streamName = "string"
  private val partitionKey = "1"
  private val region = Region.of("eu-west-1")
  private val credentials = AwsBasicCredentials.create("test-id", "test-key")
  private val builder = KinesisAsyncClient
    .builder()
    .region(this.region)
    .endpointOverride(URI.create("http://localhost:4567"))
    .credentialsProvider { credentials }
  private val client = KinesisClientUtil.createKinesisAsyncClient(builder)

  private fun createStream() {
    val request = CreateStreamRequest
      .builder()
      .streamName(streamName)
      .shardCount(1)
      .build()

    val response = client.createStream(request).get(10, TimeUnit.SECONDS)

    println(response)
  }

  private fun createRecord() {
    val request = PutRecordRequest
      .builder()
      .streamName(streamName)
      .partitionKey(partitionKey)
      .data(SdkBytes.fromString("Hello world!", Charset.defaultCharset()))
      .build()

    val response = client.putRecord(request).get(10, TimeUnit.SECONDS)

    println(response)
  }


  fun run() {
    createStream()
    createRecord()
  }


}
