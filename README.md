# Smart Resume Analyzer

[![Live Demo](https://img.shields.io/badge/🚀_Live_Demo-Railway-brightgreen)](https://smart-resume-analyzer-web-production.up.railway.app)
[![Build](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Tests](https://img.shields.io/badge/tests-31_passing-brightgreen)]()
[![Coverage](https://img.shields.io/badge/coverage-70%25-yellow)]()
[![Java](https://img.shields.io/badge/Java-17-orange)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)]()

## 🚀 Live Demo

**Try it now:** [https://smart-resume-analyzer-web-production.up.railway.app](https://smart-resume-analyzer-web-production.up.railway.app)

Upload your resume and get your ATS compatibility score in seconds!

---

## About

Domain-aware ATS resume analyzer that produces realistic compatibility scores across technical and non-technical roles.

## Scoring Philosophy

This project **intentionally avoids ML models**. Instead, it uses a deterministic, domain-aware scoring system that:

- **Classifies job intent** (technical vs business vs non-technical)
- **Classifies resume profile** (technical vs mixed vs non-technical)
- **Applies a compatibility matrix** (handles cross-domain transitions realistically)
- **Prevents unrealistic extremes** (no 0% or 100% scores)

**Why this approach?**
- ✅ **Explainable**: Every score can be traced to specific rules
- ✅ **Deterministic**: Same input always produces same output
- ✅ **Realistic**: Mirrors how real ATS systems evaluate job-candidate plausibility
- ✅ **Interview-friendly**: Easy to defend design decisions

---

## Tech Stack

- **Backend**: Spring Boot 3.x, Java 17
- **Frontend**: Vanilla HTML/CSS/JavaScript (no frameworks)
- **PDF Parsing**: Apache PDFBox 2.0.30
- **Build**: Maven (with wrapper)
- **Testing**: JUnit 5, AssertJ, MockMvc (31 tests, 100% pass rate)
- **Coverage**: JaCoCo (~70% of critical code)
- **Deployment**: Railway

## Quick Start

### Option 1: Use Live Demo
**👉 [https://smart-resume-analyzer-web-production.up.railway.app](https://smart-resume-analyzer-web-production.up.railway.app)**

### Option 2: Run Locally
```bash
# Clone and run
git clone https://github.com/likhitha-hs543/smart-resume-analyzer-web.git
cd smart-resume-analyzer-web
.\mvnw.cmd spring-boot:run

# Open browser
http://localhost:8080
```

### Usage
1. Upload resume (PDF or TXT, max 5MB)
2. Paste job description
3. View ATS score, skill analysis, and actionable recommendations

---

## How It Works

### 1. RoleIntent Classification
Jobs are classified into:
- **TECH_CORE**: SDE, AI Engineer, DevOps (skill-critical)
- **TECH_ADJACENT**: Marketing, Business Analyst, Growth (mixed)
- **NON_TECH**: Sales, HR, Operations (soft-skill focused)

### 2. ResumeProfile Detection
Resumes classified by technical signal count:
- **TECHNICAL**: 5+ technical keywords
- **MIXED**: 2-4 technical keywords  
- **NON_TECH**: <2 technical keywords

### 3. Compatibility Matrix
Role × Resume multipliers prevent absurd scores:

| Role ↓ / Resume → | TECHNICAL | MIXED | NON_TECH |
|-------------------|-----------|-------|----------|
| TECH_CORE         | 1.0       | 0.7   | 0.3      |
| TECH_ADJACENT     | 0.6       | 0.8   | 0.5      |
| NON_TECH          | 0.4       | 0.7   | 1.0      |

### 4. Final Score Calculation
```java
skillScore = matched / (matched + missing)  // with vague JD protection
finalScore = skillScore × compatibilityMultiplier × 100
finalScore = clamp(finalScore, 10, 95)  // no extremes
```

**Result**: Engineering→Business gets 40% (realistic), not 0% (naive) or 100% (absurd).

---

## API Documentation

### `POST /api/analyze`

**Request** (`multipart/form-data`):
- `resume`: File (PDF or TXT)
- `jobDescription`: String

**Response** (JSON):
```json
{
  "score": 67.0,
  "matchedSkills": ["python", "sql"],
  "missingSkills": ["git"],
  "extraSkills": ["java", "javascript", "html"],
  "suggestions": [
    "Add experience with \"git\" by mentioning it in a project or internship.",
    "You listed 9 skills not in the job description. Consider prioritizing only the most relevant ones.",
    "Add missing skills naturally within experience or project descriptions."
  ]
}
```

---

## Testing

### Run Tests
```bash
# All tests
.\mvnw.cmd test

# Specific test class
.\mvnw.cmd test -Dtest=AnalyzerControllerTest
.\mvnw.cmd test -Dtest=MatchScorerTest

# Generate coverage report
.\mvnw.cmd jacoco:report
# View: target/site/jacoco/index.html
```

### Test Coverage
- **Controller Tests**: 11 integration tests (100% pass)
- **Scorer Tests**: 19 unit tests (100% pass)
- **Total**: 31 tests, 100% pass rate, ~70% critical path coverage

---

## Deployment

### Platform
- **Hosting:** Railway.app
- **Runtime:** Java 17  
- **Framework:** Spring Boot 3.x
- **Auto-deploy:** Enabled on push to `main`
- **SSL:** HTTPS enabled
- **URL:** [https://smart-resume-analyzer-web-production.up.railway.app](https://smart-resume-analyzer-web-production.up.railway.app)

### Environment Configuration
```properties
server.port=${PORT:8080}
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

### Local Build & Deploy
```bash
# Build
.\mvnw.cmd clean package

# Run JAR
java -jar target/smart-resume-analyzer-web-0.0.1-SNAPSHOT.jar

# Access
http://localhost:8080
```

---

## Project Structure

```
com.ats/
├── SmartResumeAnalyzerWebApplication.java  # Main class
├── analyzer/                               # Core engine (framework-agnostic)
│   ├── logic/          # RoleIntent, ResumeProfile, CompatibilityMatrix
│   ├── model/          # RoleIntent, ResumeProfile enums
│   ├── scorer/         # Final ATS scoring engine
│   ├── service/        # Spring wrapper
│   └── suggestion/     # Recommendation generator
├── config/                                 # CORS configuration
└── web/                                    # API layer
    ├── controller/     # REST endpoints
    └── dto/            # Response models
```

---

## Design Decisions

### Why Not Machine Learning?
1. **No training data needed**: Rule-based logic works immediately
2. **Explainable**: Can defend every score in interviews
3. **Deterministic**: Testable and debuggable
4. **Performance**: Instant scoring, no model loading

### Why Compatibility Matrix?
Real ATS systems don't just count keywords - they evaluate:
- **Job domain**: Technical vs non-technical requirements
- **Candidate background**: Engineering vs business experience
- **Transition plausibility**: Career switches are possible but weighted differently

This is the missing piece that transforms keyword matching into intelligent scoring.

---

## What This Demonstrates

✅ **System Design Thinking**: Identified flaw in naive keyword matching  
✅ **Clean Architecture**: Framework-agnostic core with Spring wrapper  
✅ **Realistic Scoring**: No 0% or 100% extremes  
✅ **Explainability First**: Rule-based over ML black boxes  
✅ **Testing Discipline**: 31 tests, 100% pass rate, 70% coverage  
✅ **Production Deployment**: Live demo on Railway  
✅ **Honest Scope Management**: MVP discipline, no feature creep  

---

## Intentional Limitations

- ❌ No authentication (stateless MVP)
- ❌ No database (focus on algorithm)
- ❌ No UI framework (vanilla JS by design)

These are **deliberate scope decisions** for a focused portfolio project.

---

## Interview Talking Point

> *"The initial scorer gave 0% for business roles and 100% for coincidental keyword matches. I implemented a three-layer system: RoleIntent classification, ResumeProfile detection, and a CompatibilityMatrix that applies realistic multipliers. This mirrors how real ATS systems evaluate plausibility, not just keyword overlap. The system is deterministic and explainable - every score can be traced to specific rules. I also added comprehensive testing with 31 tests achieving 70% coverage of critical paths, and deployed the application to production on Railway with auto-deployment enabled."*

---

## License

MIT License - free for learning and portfolio purposes.

## Author

Built as a portfolio project demonstrating advanced system design thinking, realistic ATS scoring logic, comprehensive testing, and production deployment.
