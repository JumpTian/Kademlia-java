# Kademlia-java
The java implementation of kademlia protocol

# wiki

Usage：
```apple js
Node.Id lcoalNodeId = new Node.Id();
Node.Id peerNodeId = new Node.Id();
String ownerId = "Jump-owner";
Endpoint lcoalEndpoint = Endpoint.newInstance(lcoalNodeId, ownerId, 88888);
Endpoint peerEndpoint = Endpoint.newInstance(peerNodeId, ownerId, 98888);
endpoint.bootstrap(peerEndpoint.getLocalNode());

endpoint.bootstrap(peerEndpoint.getLocalNode());

endpoint.get(new FindParam.Builder().key(peerId).build());

```
