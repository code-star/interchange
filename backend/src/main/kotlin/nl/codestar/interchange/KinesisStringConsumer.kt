package nl.codestar.interchange

import io.netty.handler.timeout.TimeoutException
import org.slf4j.LoggerFactory
import software.amazon.awssdk.http.Protocol
import software.amazon.awssdk.http.SdkHttpConfigurationOption
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.utils.AttributeMap
import software.amazon.kinesis.common.ConfigsBuilder
import software.amazon.kinesis.coordinator.Scheduler
import software.amazon.kinesis.lifecycle.events.*
import software.amazon.kinesis.processor.ShardRecordProcessor
import software.amazon.kinesis.processor.ShardRecordProcessorFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class KinesisStringConsumer {

  private val log = LoggerFactory.getLogger(KinesisStringConsumer::class.java!!)

  private val streamName = "Roads"
  private val consumerName = "string-consumer-1"
  private val region = DefaultAwsRegionProviderChain().region

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
    .httpClient(nettyClient)

  private val kinesisClient = builder.build()

  fun run() {
    val dynamoClient = DynamoDbAsyncClient.builder().region(region).build()
    val cloudWatchClient = CloudWatchAsyncClient.builder().region(region).build()
    val configsBuilder = ConfigsBuilder(
      streamName,
      consumerName,
      kinesisClient,
      dynamoClient,
      cloudWatchClient,
      UUID.randomUUID().toString(),
      RecordProcessorFactory()
    )

    val scheduler = Scheduler(
      configsBuilder.checkpointConfig(),
      configsBuilder.coordinatorConfig(),
      configsBuilder.leaseManagementConfig(),
      configsBuilder.lifecycleConfig(),
      configsBuilder.metricsConfig(),
      configsBuilder.processorConfig(),
      configsBuilder.retrievalConfig()
    )

    val schedulerThread = Thread(scheduler)
    schedulerThread.setDaemon(true)
    schedulerThread.start()

    println("Press enter to shutdown")
    val reader = BufferedReader(InputStreamReader(System.`in`))
    try {
      reader.readLine()
    } catch (ioex: IOException) {
      log.error("Caught exception while waiting for confirm. Shutting down.", ioex)
    }

    val gracefulShutdownFuture = scheduler.startGracefulShutdown()
    log.info("Waiting up to 20 seconds for shutdown to complete.")
    try {
      gracefulShutdownFuture.get(20, TimeUnit.SECONDS)
    } catch (e: InterruptedException) {
      log.info("Interrupted while waiting for graceful shutdown. Continuing.")
    } catch (e: ExecutionException) {
      log.error("Exception while executing graceful shutdown.", e)
    } catch (e: TimeoutException) {
      log.error("Timeout while waiting for shutdown. Scheduler may not have exited.")
    }

    log.info("Completed, shutting down now.")
  }
}

class RecordProcessorFactory : ShardRecordProcessorFactory {
  override fun shardRecordProcessor(): ShardRecordProcessor {
    return RecordProcessor();
  }
}

class RecordProcessor : ShardRecordProcessor {
  private val log = LoggerFactory.getLogger(RecordProcessor::class.java!!)

  override fun shardEnded(shardEndedInput: ShardEndedInput?) {
  }

  override fun shutdownRequested(shutdownRequestedInput: ShutdownRequestedInput?) {
  }

  override fun initialize(initializationInput: InitializationInput?) {
  }

  override fun processRecords(processRecordsInput: ProcessRecordsInput) {

    log.info("Processing ${processRecordsInput.records().size} record(s)");
    processRecordsInput
      .records()
      .forEach { r ->
        val content = StandardCharsets.UTF_8.decode(r.data()).toString()
        log.info("Processing record pk: ${r.partitionKey()} -- Content ${content} -- Seq: ${r.sequenceNumber()}")
      }
  }

  override fun leaseLost(leaseLostInput: LeaseLostInput?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
