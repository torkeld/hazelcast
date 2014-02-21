

### For consideration

A ReplicatedMap *does not* support ordered writes! In case of a conflict by two nodes simultaneously written to the
same key a vector clock algorithm is used to resolve and decide on one of the values.

Due to the eventually consistent nature and the previously mentioned behaviors of this ReplicatedMap there is a
chance of reading staled data at any time. There is no read guarantee like repeatable reads.
