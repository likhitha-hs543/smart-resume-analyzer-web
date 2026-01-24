// API Configuration
const API_BASE_URL = window.location.origin;

// Score Thresholds
const SCORE_THRESHOLDS = {
    STRONG: 80,
    GOOD: 60
};

/**
 * Main function to analyze resume against job description
 * @returns {Promise<void>}
 */
async function analyzeResume() {
    const resumeInput = document.getElementById("resumeFile");
    const jdInput = document.getElementById("jobDescription");
    const resultsDiv = document.getElementById("results");
    const analyzeBtn = document.getElementById("analyzeBtn");

    if (resumeInput.files.length === 0) {
        alert("Please upload a resume file.");
        return;
    }

    if (!jdInput.value.trim()) {
        alert("Please paste the job description.");
        return;
    }

    const formData = new FormData();
    formData.append("resume", resumeInput.files[0]);
    formData.append("jobDescription", jdInput.value);

    // Loading state
    analyzeBtn.disabled = true;
    analyzeBtn.textContent = "Analyzing...";
    resultsDiv.innerHTML = '<div class="loading">Analyzing your resume...</div>';
    resultsDiv.classList.add('show');

    try {
        const response = await fetch(`${API_BASE_URL}/api/analyze`, {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            throw new Error("Analysis failed");
        }

        const result = await response.json();
        displayResults(result);

    } catch (error) {
        console.error('Analysis error:', error);
        resultsDiv.innerHTML = `
            <div class="card">
                <strong>Error:</strong> Unable to analyze resume. 
                ${error.message ? `<br><small style="color: #64748b; margin-top: 8px;">${error.message}</small>` : 'Please try again.'}
            </div>
        `;
    } finally {
        analyzeBtn.disabled = false;
        analyzeBtn.textContent = "Check ATS Match";
    }
}

/**
 * Display analysis results in the UI
 * @param {Object} result - Analysis result from API
 */
function displayResults(result) {
    const score = result.score.toFixed(0);
    const matchCount = result.matchedSkills.length;
    const missingCount = result.missingSkills.length;

    // Generate summary text
    let summaryText = "";
    if (score >= SCORE_THRESHOLDS.STRONG) {
        summaryText = "Strong match! Your resume aligns well with the job requirements.";
    } else if (score >= SCORE_THRESHOLDS.GOOD) {
        summaryText = "Good match, but key skill gaps may reduce ATS ranking.";
    } else {
        summaryText = "Significant gaps detected. Consider adding missing skills to improve ATS performance.";
    }

    const resultsHTML = `
            <div class="result-summary">
                <strong>Your resume matches ${score}% of the job requirements.</strong>
                <p>${summaryText}</p>
            </div>

            <div class="score-card">
                <div class="score-label">ATS Match Score</div>
                <div class="score-value">${score}%</div>
            </div>

            <div class="grid">
                <div class="metric-card">
                    <div class="metric-header">
                        <span class="metric-title">Matched Skills</span>
                        <span class="metric-count">${matchCount}</span>
                    </div>
                    <div class="metric-list">
                        <ul>${result.matchedSkills.map(s => `<li>${s}</li>`).join("")}</ul>
                    </div>
                </div>

                <div class="metric-card">
                    <div class="metric-header">
                        <span class="metric-title">Missing Skills</span>
                        <span class="metric-count">${missingCount}</span>
                    </div>
                    <div class="metric-list">
                        <ul>${result.missingSkills.map(s => `<li>${s}</li>`).join("")}</ul>
                    </div>
                </div>

                <div class="metric-card">
                    <div class="metric-header">
                        <span class="metric-title">Extra Skills</span>
                        <span class="metric-count">${result.extraSkills.length}</span>
                    </div>
                    <div class="metric-list">
                        <ul>${result.extraSkills.map(s => `<li>${s}</li>`).join("")}</ul>
                    </div>
                </div>
            </div>

            <div class="suggestions-card">
                <h3>What to Fix First</h3>
                ${result.suggestions.map((s, idx) => {
                const isPrimary = idx === 0;
                const isTip = s.includes('instead of listing') || s.includes('Strong alignment') || s.includes('Focus on matching');
                const className = isPrimary ? 'recommendation primary' : (isTip ? 'recommendation tip' : 'recommendation secondary');
                const icon = isTip ? '💡 ' : '';
                return `<div class="${className}">${icon}${s}</div>`;
            }).join("")}
            </div>
        `;

    document.getElementById("results").innerHTML = resultsHTML;
    document.getElementById("results").classList.add('show');
}
