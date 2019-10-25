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
    