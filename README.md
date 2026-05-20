# In-Memory Cache System in Java

A hybrid in-memory cache system built in Java supporting **LRU eviction** and **dual TTL expiration strategies (Lazy + Active)**.

This project was designed to understand real-world backend caching concepts such as:

- Cache eviction policies
- Expiration handling
- Background cleanup systems
- PriorityQueue-based scheduling
- Thread-safe shared data access

Inspired by backend systems used in technologies like **Redis** and **Caffeine**.

---

## Features

- **O(1) get/set operations** using HashMap + Doubly Linked List
- **LRU (Least Recently Used) eviction policy**
- Supports **expiring and non-expiring keys**
- **Lazy TTL expiration** during reads
- **Active TTL expiration** using background cleanup
- **PriorityQueue (Min-Heap)** for expiry ordering
- **Duplicate-safe TTL handling**
- **Thread-safe cache operations**
- Graceful scheduler shutdown support

---

## Architecture

The cache combines multiple internal data structures:

| Component | Purpose |
|----------|---------|
| **HashMap** | O(1) key lookup |
| **Doubly Linked List** | Maintains LRU ordering |
| **PriorityQueue (Min-Heap)** | Tracks earliest expiring entries |
| **Scheduled Background Thread** | Removes expired keys proactively |

---

## How It Works

### 1. LRU Eviction

The cache tracks usage order using a **Doubly Linked List**:

- Most recently used → head
- Least recently used → tail

When cache capacity is full:

- Expired keys are cleaned first
- If still full → least recently used key is evicted

---

### 2. Lazy TTL Expiration

Expiration is checked during `get()`:

- If key is expired → removed immediately
- Prevents stale reads

---

### 3. Active TTL Expiration

A background scheduler runs periodically:

- Uses **PriorityQueue** to fetch earliest expiry
- Removes expired entries proactively
- Prevents stale keys from occupying memory

---

## Design Challenges Solved

### Duplicate Expiry Entries Problem

Updating a key with a new TTL creates:

- Old expiry record
- New expiry record

Both exist in PriorityQueue.

### Solution

Before deleting:

```java
if (cacheMap.get(node.getKey()) == node)
```

Only the latest valid node is removed.

---

### Lazy TTL Memory Retention Problem

Lazy expiration removes expired keys only when accessed.

### Solution

Added:

- Background cleanup thread
- PriorityQueue-based proactive expiration

---

### Thread Safety Problem

Multiple threads can modify:

- HashMap
- Doubly Linked List
- PriorityQueue

### Solution

Added synchronized critical sections using a shared lock.

---

## Time Complexity

| Operation      | Complexity |
|----------------|------------|
| `get()`        | O(1)       |
| `set()`        | O(1)       |
| TTL insert     | O(log n)   |
| Expiry cleanup | O(log n)   |

---

## Example Usage

```java
Cache cache = new Cache(2);

cache.set(1, 100, 5);   // expires in 5 seconds
cache.set(2, 200);

System.out.println(cache.get(1));

cache.shutdown();
```

---

## Project Structure

```text
src/
 ├── Cache.java
 ├── Node.java
 ├── DoublyLinkedList.java
 ├── EvictionManager.java
 └── Main.java
```

---

## Tradeoffs / Limitations

Current implementation has some tradeoffs:

- Fixed cleanup interval (2 seconds)
- Coarse-grained synchronization
- Single-process in-memory cache only
- No persistence
- No cache metrics yet

---

## Future Improvements

Possible next upgrades:

- Fine-grained locking
- Dynamic expiry scheduling
- Hit/miss rate metrics
- Persistence support
- Distributed cache design

---

## Key Learnings

This project helped explore:

- Data structure integration
- Backend caching design
- Expiration strategies
- Background task scheduling
- Concurrency control
- Real-world system tradeoffs

---

## Shutdown

This cache uses a background scheduler.

Always call:

```java
cache.shutdown();
```

to properly stop background cleanup threads.

---

## Technologies Used

- **Java**
- **HashMap**
- **Doubly Linked List**
- **PriorityQueue**
- **ScheduledExecutorService**
- **Synchronization / Concurrency**

---

## Repository

GitHub: https://github.com/karthikeya-crypto/in-memory-cache-java
