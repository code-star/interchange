# Infra - report out Day 1

* Created EKS Cluster
    * See /aws/README.md for details
    * Lessons learned
        * If you want to use the `eksctl`, use it from the start (it cannot cope with cluster created from the web console)
        * Ensure there are enough nodes to start with (cluster itself creates at least 6 pods, so 1 node with max 4 pods is not enough)
* Created `tryout-service` that proves our cluster is up and running, and services can be accessed:
    * http://a59da77c3f71a11e9a06a0640b6daccb-1632254577.eu-west-1.elb.amazonaws.com/
* Created a Kinesis stream called `Roads`
    * `aws kinesis list-streams`
        ```json
               {
                   "StreamNames": [
                       "Roads"
                   ]
               }
        ```    
* Prepared github actions
    * See `github-workflows` branch
    
## Update 28 oct
After the weekend, the AWS cost dashboard showed about $20 in cost.
Cleaned up the EKS cluster (the majority of the cost) by executing:

`eksctl delete cluster --name=interchange-cluster`

## Update 1 Nov
The Kinesis stream has an hourly cost, even if it is not being used. Decided to delete it (saved current output of describe-stream)

`aws kinesis describe-stream --stream-name Roads`

Result:
```
{
    "StreamDescription": {
        "Shards": [
            {
                "ShardId": "shardId-000000000000",
                "HashKeyRange": {
                    "StartingHashKey": "0",
                    "EndingHashKey": "340282366920938463463374607431768211455"
                },
                "SequenceNumberRange": {
                    "StartingSequenceNumber": "49600767373986662719993077662419282190328424006523289602"
                }
            }
        ],
        "StreamARN": "arn:aws:kinesis:eu-west-1:182176061631:stream/Roads",
        "StreamName": "Roads",
        "StreamStatus": "ACTIVE",
        "RetentionPeriodHours": 24,
        "EnhancedMonitoring": [
            {
                "ShardLevelMetrics": []
            }
        ],
        "EncryptionType": "NONE",
        "KeyId": null,
        "StreamCreationTimestamp": 1572004832.0
    }
}
```

`aws kinesis delete-stream --stream-name Roads`