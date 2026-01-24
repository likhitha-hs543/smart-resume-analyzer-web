# 🎯 Smart Resume Analyzer

[![Live Demo](https://img.shields.io/badge/🚀_Live_Demo-Try_Now-brightgreen)](https://smart-resume-analyzer-web-production.up.railway.app)
[![Tests](https://img.shields.io/badge/tests-34_passing-brightgreen)]()
[![Coverage](https://img.shields.io/badge/coverage-70%25-yellow)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**Domain-aware ATS resume scoring system with realistic compatibility analysis.**

> Upload your resume and job description to get an instant ATS compatibility score with actionable feedback. Unlike traditional keyword matchers, this analyzer understands career transitions and provides realistic scores.

---

## 🚀 **[Try Live Demo →](https://smart-resume-analyzer-web-production.up.railway.app)**

---

## ✨ Key Features

### 🎯 **Smart Scoring Algorithm**
- **Domain-aware compatibility** - Understands career transitions (backend → design, frontend → backend)
- **Realistic scores (10-95%)** - No extremes, no score inflation
- **Deterministic & explainable** - Every score traceable to specific rules
- **Compatibility matrix** - Role-specific scoring multipliers

### 📊 **Comprehensive Analysis**
- Matched, missing, and extra skills breakdown
- Actionable suggestions for resume improvement
- Cross-domain transition support
- Realistic ATS behavior simulation

### 🔒 **Privacy & Security**
- **No data storage** - Files processed in-memory only
- **HTTPS encryption** - Secure communication
- **5MB file limit** - Reasonable size constraints
- **PDF & TXT support** - Flexible file formats

---

## 🏗️ Architecture

```
┌──────────┐     ┌────────────┐     ┌──────────────┐
│  Browser │────▶│  Frontend  │────▶│  Controller  │
│          │◀────│  (HTML/JS) │◀────│  (Spring)    │
└──────────┘     └────────────┘     └──────────────┘
                                            │
                        ┌───────────────────┼───────────────────┐
                        │                   │                   │
                   ┌────▼────┐        ┌────▼────┐        ┌────▼────┐
                   │  PDF    │        │Analyzer │        │ Match   │
                   │ Parser  │        │ Service │        │ Scorer  │
                   └─────────┘        └─────────┘        └─────────┘
                                            │
                        ┌───────────────────┼───────────────────┐
                        │                   │                   │
                   ┌────▼────┐        ┌────▼────┐        ┌────▼────┐
                   │Keyword  │        │  Role   │        │Compat.  │
                   │Extract  │        │Detector │        │ Matrix  │
                   └─────────┘        └─────────┘        └─────────┘
```

### Scoring Logic

The **Compatibility Matrix** assigns multipliers based on career domain combinations:

| Resume Profile ↓ / Job Role → | Backend | Frontend | Full-Stack | Design |
|-------------------------------|---------|----------|------------|--------|
| **Backend**                   | 0.52    | 0.28     | 0.45       | 0.18   |
| **Frontend**                  | 0.28    | 0.52     | 0.45       | 0.35   |
| **Full-Stack**                | 0.45    | 0.45     | 0.52       | 0.30   |

**Example:** Backend engineer → Backend role = 52% multiplier (strong match)  
Backend engineer → Design role = 18% multiplier (realistic cross-domain penalty)

---

## 🧪 Testing

- **34 comprehensive tests** (100% pass rate)
- **~70% code coverage** (focused on critical paths)
- **Integration + Unit + Exception handling tests**

```bash
# Run all tests
.\mvnw.cmd test

# Generate coverage report
.\mvnw.cmd jacoco:report

# View coverage
start target/site/jacoco/index.html
```

**Test Breakdown:**
- ✅ 11 integration tests (API endpoints, CORS, validation)
- ✅ 19 unit tests (scoring algorithm, boundaries, edge cases)
- ✅ 4 exception handling tests (error responses, file validation)

---

## 📡 API Documentation

### `POST /api/analyze`
Analyze resume against job description and return compatibility score.

**Request:**
```bash
curl -X POST https://smart-resume-analyzer-web-production.up.railway.app/api/analyze \
  -F "resume=@resume.pdf" \
  -F "jobDescription=Backend developer with Java and Spring Boot"
```

**Response (200 OK):**
```json
{
  "score": 43.0,
  "matchedSkills": ["java", "spring boot", "sql", "docker"],
  "missingSkills": ["kubernetes", "aws"],
  "extraSkills": ["python", "mongodb"],
  "suggestions": [
    "Add missing skills: kubernetes, aws",
    "Strong match for backend development roles",
    "Consider highlighting your Docker experience"
  ]
}
```

**Error Response (400/413/500):**
```json
{
  "error": "File Too Large",
  "message": "Maximum file size is 5MB. Please upload a smaller file.",
  "status": "413"
}
```

### `GET /api/health`
Health check endpoint for monitoring and deployment verification.

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "Smart Resume Analyzer",
  "version": "1.0.0",
  "timestamp": "2026-01-24T19:40:00",
  "uptime": 3600000
}
```

---

## 💻 Local Development

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Quick Start

```bash
# Clone repository
git clone https://github.com/likhitha-hs543/smart-resume-analyzer-web.git
cd smart-resume-analyzer-web

# Run application
.\mvnw.cmd spring-boot:run

# Access at
http://localhost:8080
```

### Project Structure

```
src/
├── main/
│   ├── java/com/ats/
│   │   ├── analyzer/           # Core business logic
│   │   │   ├── extractor/      # Keyword extraction
│   │   │   ├── logic/          # Domain detection & compatibility matrix
│   │   │   ├── matcher/        # Skill matching
│   │   │   ├── model/          # Data models & enums
│   │   │   ├── parser/         # Text cleaning
│   │   │   ├── scorer/         # Scoring algorithm
│   │   │   ├── service/        # Service layer
│   │   │   └── suggestion/     # Suggestion generation
│   │   ├── config/             # Spring configuration (CORS)
│   │   └── web/                # Controllers, DTOs, exceptions
│   └── resources/
│       ├── static/             # Frontend files (HTML/CSS/JS)
│       ├── skills.txt          # Skills dictionary
│       └── application.properties
└── test/
    └── java/com/ats/           # Comprehensive test suite
```

---

## 🐳 Docker

```bash
# Build image
docker build -t resume-analyzer .

# Run container
docker run -p 8080:8080 resume-analyzer

# Access
http://localhost:8080
```

---

## 🚀 Deployment

**Platform:** Railway.app  
**URL:** https://smart-resume-analyzer-web-production.up.railway.app  
**Auto-deploy:** Enabled on push to `main`  
**SSL:** HTTPS automatic  
**Health Check:** Configured at `/api/health`

### Environment Configuration

```properties
server.port=${PORT:8080}
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=5MB
```

---

## 🛠️ Tech Stack

**Backend:**  
- Java 17 - Modern Java features
- Spring Boot 3.x - Web framework
- Apache PDFBox - PDF text extraction
- Maven - Dependency management

**Frontend:**  
- Vanilla JavaScript - Fast, no frameworks
- HTML5 & CSS3 - Modern web standards
- Responsive Design - Mobile-first approach

**Testing:**  
- JUnit 5 - Unit testing framework
- Spring MockMvc - Integration testing
- AssertJ - Fluent assertions
- JaCoCo - Code coverage reporting

**DevOps:**  
- Railway - Cloud deployment
- Docker - Containerization
- Git - Version control

---

## 📊 Key Insights

### Conservative Scoring Behavior
Real-world test results demonstrate realistic AT scoring:

| Scenario | Score | Analysis |
|----------|-------|----------|
| Backend → Backend (good match) | 39-43% | Realistic, not inflated |
| Backend → Frontend (mismatch) | 10-15% | Appropriate penalty |
| Career transition (Frontend → Backend) | 17-20% | Conservative, realistic |
| Vague JD (generic requirements) | 15-20% | Penalized correctly |
| No skills match | 10% | Minimum floor enforced |
| Design mismatch | 10-12% | Heavy penalty applied |

**This mirrors real ATS systems** - conservative scoring, not generous inflation.

### Algorithm Features
- ✅ **No score inflation** - Maximum 95%, never 100%
- ✅ **No zero scores** - Minimum 10%, never 0%
- ✅ **Domain-aware** - Understands technical vs non-technical roles
- ✅ **Deterministic** - Same input = same output, always
- ✅ **Explainable** - Every score traceable to specific rules

---

## 🤝 Contributing

Contributions welcome! Please follow these steps:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

### Guidelines
- ✅ All tests must pass (`.\mvnw.cmd test`)
- ✅ Maintain existing code style
- ✅ Add tests for new features
- ✅ Update documentation as needed

---

## 📄 License

MIT License - see [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Likhitha**  
- GitHub: [@likhitha-hs543](https://github.com/likhitha-hs543)
- Project: [smart-resume-analyzer-web](https://github.com/likhitha-hs543/smart-resume-analyzer-web)
- Live Demo: [Try Now](https://smart-resume-analyzer-web-production.up.railway.app)

---

## 🙏 Acknowledgments

- **Apache PDFBox** - PDF text extraction
- **Spring Boot** - Web framework and ecosystem
- **Railway** - Deployment platform
- **GitHub** - Version control and hosting

---

## 📈 Project Stats

![Java](https://img.shields.io/badge/Java-86.9%25-orange)
![CSS](https://img.shields.io/badge/CSS-5.7%25-blue)
![JavaScript](https://img.shields.io/badge/JavaScript-5.2%25-yellow)
![HTML](https://img.shields.io/badge/HTML-1.9%25-red)

**Built with ❤️ for better job applications**
