# Smart Resume Analyzer - Web Interface

Web wrapper for the [Smart Resume Analyzer](https://github.com/likhitha-hs543/smart-resume-analyzer) engine. This Spring Boot application exposes the analyzer via REST API and provides a browser-based UI.

## Architecture

```
[ Browser (HTML/CSS/JS) ]
          ↓
[ Spring Boot REST API ]
          ↓  
[ Analyzer Engine (Framework-Agnostic) ]
```

**Key Design**: The core analyzer logic remains framework-free. Only the service layer touches Spring.

## Tech Stack

- **Backend**: Spring Boot 3.x, Java 17
- **Frontend**: Vanilla HTML/CSS/JavaScript (no frameworks)
- **PDF Parsing**: Apache PDFBox 2.0.30
- **Build**: Maven (with wrapper)

## How to Run

### Prerequisites
- Java 17 or higher

### Quick Start

```bash
# Clone the repository
git clone https://github.com/likhitha-hs543/smart-resume-analyzer-web.git
cd smart-resume-analyzer-web

# Run the application
.\mvnw.cmd spring-boot:run

# Open browser
http://localhost:8080
```

### Usage
1. Upload your resume (PDF or TXT)
2. Paste the job description
3. Click "Analyze"
4. View ATS score, matched/missing/extra skills, and suggestions

## API Documentation

### `POST /api/analyze`

Analyzes resume against job description.

**Request**:
- Content-Type: `multipart/form-data`
- Parameters:
  - `resume`: File (PDF or TXT)
  - `jobDescription`: String

**Response** (JSON):
```json
{
  "score": 33.0,
  "matchedSkills": ["java", "sql", "git"],
  "missingSkills": ["spring", "docker", "aws"],
  "extraSkills": ["python", "flask"],
  "suggestions": [
    "Consider adding experience with: aws, docker, spring",
    "Align your additional skills (2 found) with the job requirements"
  ]
}
```

## Project Structure

```
src/main/java/com/ats/
├── web/
│   ├── controller/         # REST endpoints
│   ├── dto/               # API response models
│   └── SmartResumeAnalyzerWebApplication.java
└── analyzer/              # Core engine (framework-agnostic)
    ├── input/             # Resume/JD loaders
    ├── parser/            # Text cleaning
    ├── extractor/         # Skill extraction
    ├── matcher/           # Skill matching
    ├── scorer/            # ATS scoring
    ├── suggestion/        # Suggestion generation
    └── service/           # Spring wrapper
```

## Relationship to Core Analyzer

This project is a **web wrapper** around the [smart-resume-analyzer](https://github.com/likhitha-hs543/smart-resume-analyzer) CLI tool. The analyzer logic was copied and repackaged under `com.ats.analyzer.*` to maintain framework independence.

**Design decision**: The core logic stays frozen and framework-agnostic. Only `AnalyzerService` is Spring-aware.

## What This Project Demonstrates

✅ **Clean Architecture**: Separation of concerns (web layer vs. domain logic)  
✅ **Framework Independence**: Core logic doesn't depend on Spring  
✅ **REST API Design**: Proper endpoint structure and error handling  
✅ **MVP Discipline**: No auth, database, or unnecessary complexity  
✅ **Progressive Commits**: 5 deliberate commits showing incremental build  

## Commit History

```
1. Initialize Spring Boot project for Smart Resume Analyzer web app
2. Add basic HTML UI for resume analysis
3. Integrate core analyzer logic as reusable service layer
4. Expose resume analysis via REST API
5. Connect frontend UI to analysis API and display results
```

Each commit represents a complete, buildable checkpoint.

## Limitations (Intentional MVP Scope)

- ❌ No authentication/authorization
- ❌ No database (stateless API)
- ❌ No user accounts or history
- ❌ No deployment configuration
- ❌ No UI frameworks (vanilla JS by design)

These are **deliberate scope decisions** for a focused MVP, not missing features.

## Future Enhancements

- Error handling improvements (user-friendly messages)
- Loading states and disabled buttons during analysis
- File size validation before upload
- CORS configuration for separate frontend deployment
- Batch analysis support
- Export results as PDF/JSON

## License

MIT License - feel free to use for learning and portfolio purposes.

## Author

Built as a portfolio project demonstrating:
- **Reusable architecture**: Engine → API → UI
- **Framework-agnostic design**: Core logic independent of Spring
- **Progressive development**: Clean commit history
- **Honest scope management**: MVP discipline

---

**Interview Talking Point**:  
*"I built the analyzer as a reusable Java engine first, then wrapped it with a Spring Boot REST API and minimal web UI. The core logic stayed framework-agnostic, making it reusable across interfaces."*
