package com.ats;

/**
 * Shared test constants and sample data for unit and integration tests.
 * This class provides reusable test data to ensure consistency across test
 * cases.
 */
public class TestConstants {

    // ==================== SAMPLE RESUMES ====================

    public static final String SAMPLE_BACKEND_RESUME = """
            Software Engineer with 5+ years of experience in backend development.

            Technical Skills:
            - Programming: Java, Python, SQL
            - Frameworks: Spring Boot, Hibernate, Django
            - Cloud: AWS, Docker, Kubernetes
            - Databases: MySQL, PostgreSQL, MongoDB
            - Tools: Git, Maven, Jenkins

            Experience:
            - Developed microservices architecture using Spring Boot
            - Designed and implemented REST APIs
            - Worked with cloud deployment on AWS
            - Database optimization and query tuning
            """;

    public static final String SAMPLE_FRONTEND_RESUME = """
            Frontend Developer with 4 years of experience building responsive web applications.

            Skills:
            - JavaScript, TypeScript, React, Angular
            - HTML5, CSS3, SASS, Bootstrap
            - Redux, Webpack, npm
            - REST API integration
            - Responsive design and UX

            Projects:
            - Built single-page applications with React
            - Implemented state management with Redux
            - Created reusable component libraries
            """;

    public static final String SAMPLE_FULLSTACK_RESUME = """
            Full Stack Developer with experience in both frontend and backend technologies.

            Frontend: React, Angular, JavaScript, HTML, CSS
            Backend: Java, Spring Boot, Node.js, Express
            Database: MySQL, MongoDB
            DevOps: Docker, Kubernetes, AWS, CI/CD

            Developed end-to-end features from database to UI.
            """;

    // ==================== SAMPLE JOB DESCRIPTIONS ====================

    public static final String SAMPLE_JD_BACKEND = """
            Backend Developer Position

            We are looking for an experienced Backend Developer to join our team.

            Required Skills:
            - Java and Spring Boot
            - REST API development
            - Microservices architecture
            - Docker and Kubernetes
            - SQL databases (MySQL or PostgreSQL)
            - AWS cloud experience

            Nice to have:
            - Python experience
            - NoSQL databases
            - CI/CD pipeline setup
            """;

    public static final String SAMPLE_JD_FRONTEND = """
            Frontend Engineer - React Specialist

            Required:
            - 3+ years of React development
            - JavaScript/TypeScript proficiency
            - HTML5, CSS3, responsive design
            - State management (Redux or MobX)
            - REST API integration
            - Git version control

            Preferred:
            - Next.js or Gatsby experience
            - Testing with Jest/React Testing Library
            - UI/UX design skills
            """;

    public static final String SAMPLE_JD_FULLSTACK = """
            Full Stack Developer

            We need a versatile developer comfortable with both frontend and backend.

            Requirements:
            - Frontend: React, JavaScript, HTML, CSS
            - Backend: Java, Spring Boot, Node.js
            - Database: SQL and NoSQL
            - Cloud: AWS or Azure
            - Docker, Kubernetes
            - RESTful API design

            Looking for someone who can work across the entire stack.
            """;

    public static final String SAMPLE_JD_DATA = """
            Data Engineer Position

            Required Skills:
            - Python for data processing
            - SQL (advanced level)
            - Apache Spark, Hadoop
            - Data warehousing
            - ETL pipeline development
            - AWS (S3, Redshift, EMR)

            Preferred:
            - Machine learning basics
            - Airflow or similar orchestration tools
            """;

    public static final String SAMPLE_JD_DEVOPS = """
            DevOps Engineer

            Essential:
            - Docker and Kubernetes
            - AWS/Azure cloud platforms
            - CI/CD pipeline setup (Jenkins, GitLab CI)
            - Infrastructure as Code (Terraform, CloudFormation)
            - Monitoring (Prometheus, Grafana)
            - Linux administration

            Nice to have:
            - Python or Bash scripting
            - Security best practices
            """;

    public static final String SAMPLE_JD_DESIGN = """
            UI/UX Designer

            Requirements:
            - Figma, Sketch,Adobe XD proficiency
            - User research and testing
            - Wireframing and prototyping
            - Design systems creation
            - Responsive web design
            - Collaboration with developers

            Bonus:
            - HTML/CSS knowledge
            - Animation and micro-interactions
            """;

    // ==================== VAGUE JOB DESCRIPTION ====================

    public static final String SAMPLE_JD_VAGUE = """
            Software Developer

            We are looking for a talented developer to join our team.
            Good problem-solving skills required.
            """;

    // ==================== TEST FILE NAMES ====================

    public static final String TEST_RESUME_PDF_NAME = "test-resume.pdf";
    public static final String TEST_RESUME_TXT_NAME = "test-resume.txt";
    public static final String TEST_RESUME_INVALID_NAME = "test-resume.docx";

    // ==================== HTTP CONSTANTS ====================

    public static final String CONTENT_TYPE_PDF = "application/pdf";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String MULTIPART_FORM_PARAM_RESUME = "resume";
    public static final String MULTIPART_FORM_PARAM_JOB_DESC = "jobDescription";

    // ==================== EXPECTED SCORES (for testing) ====================

    public static final int MIN_SCORE = 10;
    public static final int MAX_SCORE = 95;
    public static final int PERFECT_MATCH_THRESHOLD = 85;
    public static final int GOOD_MATCH_THRESHOLD = 60;
    public static final int POOR_MATCH_THRESHOLD = 40;

    // ==================== PRIVATE CONSTRUCTOR ====================

    private TestConstants() {
        // Utility class, prevent instantiation
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
