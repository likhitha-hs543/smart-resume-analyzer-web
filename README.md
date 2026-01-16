# Smart Resume Analyzer - Web Interface

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

## Quick Start

```bash
# Clone and run
git clone https://github.com/likhitha-hs543/smart-resume-analyzer-web.git
cd smart-resume-analyzer-web
.\mvnw.cmd spring-boot:run

# Open browser
http://localhost:8080
```

### Usage
1. Upload resume (PDF or TXT)
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
✅ **Honest Scope Management**: MVP discipline, no feature creep  

---

## Intentional Limitations

- ❌ No authentication (stateless MVP)
- ❌ No database (focus on algorithm)
- ❌ No deployment config (local development)
- ❌ No UI framework (vanilla JS by design)

These are **deliberate scope decisions** for a focused portfolio project.

---

## Interview Talking Point

> *"The initial scorer gave 0% for business roles and 100% for coincidental keyword matches. I implemented a three-layer system: RoleIntent classification, ResumeProfile detection, and a CompatibilityMatrix that applies realistic multipliers. This mirrors how real ATS systems evaluate plausibility, not just keyword overlap. The system is deterministic and explainable - every score can be traced to specific rules."*

---

## License

MIT License - free for learning and portfolio purposes.

## Author

Built as a portfolio project demonstrating advanced system design thinking and realistic ATS scoring logic.
