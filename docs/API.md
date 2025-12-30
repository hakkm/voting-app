# API Documentation

## Voting App REST API

Base URL: `http://localhost:8080/api`

---

## Endpoints

### 1. Cast a Vote

**POST** `/api/votes`

Cast a new vote for a candidate.

**Request Body:**
```json
{
  "voterId": "voter123",
  "candidateId": "candidate1"
}
```

**Success Response (201 Created):**
```json
{
  "voterId": "voter123",
  "candidateId": "candidate1",
  "timestamp": 1703952000000,
  "message": "Vote successfully recorded"
}
```

**Error Responses:**

*400 Bad Request* - Invalid input
```json
{
  "status": 400,
  "message": "Voter ID cannot be blank",
  "error": "Validation Failed",
  "timestamp": "2025-12-30T14:30:00"
}
```

*400 Bad Request* - Duplicate vote
```json
{
  "status": 400,
  "message": "Voter voter123 has already cast a vote",
  "error": "Validation Failed",
  "timestamp": "2025-12-30T14:30:00"
}
```

---

### 2. Get Voting Results

**GET** `/api/votes/results?strategy={strategy}`

Retrieve vote tallies using specified counting strategy.

**Query Parameters:**
- `strategy` (optional): Counting method
  - `plurality` (default) - Simple majority
  - `runoff` - Two-round runoff system
  - `borda` - Borda count scoring
  - `approval` - Approval voting

**Success Response (200 OK):**
```json
{
  "results": {
    "candidate1": 5,
    "candidate2": 3,
    "candidate3": 2
  },
  "totalVotes": 10,
  "strategy": "plurality"
}
```

**Examples:**
```bash
# Default plurality counting
GET /api/votes/results

# Two-round runoff
GET /api/votes/results?strategy=runoff

# Borda count
GET /api/votes/results?strategy=borda

# Approval voting
GET /api/votes/results?strategy=approval
```

---

### 3. Get All Votes (Admin)

**GET** `/api/votes`

Retrieve all votes cast in the system.

**Success Response (200 OK):**
```json
[
  {
    "voterId": "voter1",
    "candidateId": "candidate1",
    "timestamp": 1703952000000
  },
  {
    "voterId": "voter2",
    "candidateId": "candidate2",
    "timestamp": 1703952100000
  }
]
```

---

### 4. Clear All Votes (Admin)

**DELETE** `/api/votes`

Remove all votes from the system. **This operation is irreversible.**

**Success Response (200 OK):**
```
All votes have been cleared
```

---

## Error Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success |
| 201 | Created (vote cast successfully) |
| 400 | Bad Request (validation error) |
| 500 | Internal Server Error |

---

## Validation Rules

### Voter ID
- Must not be blank
- Must not be null
- Cannot vote twice (duplicate prevention)

### Candidate ID
- Must not be blank
- Must not be null

---

## Counting Strategies

### Plurality (Default)
Simple majority voting - each vote counts as one point for a candidate. The candidate with the most votes wins.

### Two-Round Runoff
If no candidate receives >50% of votes, the top two candidates proceed to a runoff. This implementation returns the top 2 candidates when no majority is achieved.

### Borda Count
Point-based system where votes are weighted. Each vote = 10 points. This is a simplified version; true Borda count requires ranked ballots.

### Approval Voting
Each voter can approve multiple candidates. In our implementation with single-choice votes, it functions similarly to plurality but demonstrates the pattern for future multi-approval support.

---

## Example Usage

### Using cURL

**Cast a vote:**
```bash
curl -X POST http://localhost:8080/api/votes \
  -H "Content-Type: application/json" \
  -d '{"voterId":"voter1","candidateId":"candidate1"}'
```

**Get results:**
```bash
curl http://localhost:8080/api/votes/results
```

**Get results with runoff strategy:**
```bash
curl http://localhost:8080/api/votes/results?strategy=runoff
```

**Get all votes:**
```bash
curl http://localhost:8080/api/votes
```

**Clear all votes:**
```bash
curl -X DELETE http://localhost:8080/api/votes
```

### Using HTTPie

**Cast a vote:**
```bash
http POST localhost:8080/api/votes voterId=voter1 candidateId=candidate1
```

**Get results:**
```bash
http GET localhost:8080/api/votes/results
```

**Get results with Borda count:**
```bash
http GET localhost:8080/api/votes/results strategy==borda
```

---

## Health Check

Spring Boot Actuator provides health endpoints:

```bash
GET /actuator/health
```

Response:
```json
{
  "status": "UP"
}
```

---

## Development

### Run Locally
```bash
mvn spring-boot:run
```

### Run with Docker
```bash
docker-compose up voting-app
```

### Run Tests
```bash
mvn test
```

### Generate Coverage Report
```bash
mvn jacoco:report
```

---

**Last Updated:** December 30, 2025
**Version:** 1.0.0
