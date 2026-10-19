# QuizEZ Project

A desktop quiz application developed as a coursework assignment for the "Technical Training 1" (Szkolenie Techniczne 1) course.  
Project created by a two-person team.

---

### About the Project
QuizEZ is a desktop application designed for creating, managing, and taking quizzes across various knowledge domains. It is intended for individuals who want to test their knowledge, as well as for students and teachers for classroom use.

<img width="1552" height="878" alt="S2" src="https://github.com/user-attachments/assets/c47ac86a-cb0a-4fa9-b31d-17cc81df162f" />


### Features
- User account registration, login, guest access mode, and profile customization.
- Selecting quizzes from an existing list or creating new ones.
- Taking quizzes with optional time limits.
- Quiz summary, final score calculation, and comparison with previous attempts.
- Editing and deleting existing quizzes.
- Configurable difficulty levels and time limits.
- Browsing historical results for previously completed quizzes.

### Quiz Categories
- Video Games
- Geography
- World Religions
- Movies and TV Shows
- World History
- Space
- Exact Sciences
- Unspecified

<img width="1230" height="677" alt="S3" src="https://github.com/user-attachments/assets/dd2bba87-ea75-4d8e-9ffb-d314ccff8ab0" />

<img width="1575" height="861" alt="S4" src="https://github.com/user-attachments/assets/4a80b714-483d-40f5-b07b-fb65acde31a8" />


### Non-Technical Requirements
- Intuitive user interface suitable for various age groups.
- Offline support (local operation).
- Scalability.

### Technical Requirements
- **Exception Handling:**
  - Database connection loss.
  - Invalid input or output data.
  - Attempted operations on non-existent resources (e.g., missing quiz or user).
- **Data Validation:**
  - User registration checks (e.g., username uniqueness, minimum password length).
  - Login credential validation.
  - Input validation during quiz creation and editing (e.g., questions cannot be empty).
- **Data Persistence (Read/Write):**
  - Users: credentials, accounts, score history.
  - Quizzes: questions, answers, categories, difficulty levels, time limits.
  - Results: completion date, score, percentage of correct answers.

### Technologies Used
- **Java** - Primary programming language
- **JavaFX** - User interface framework
- **SQLite** - Application database
- **Maven** - Project build and dependency management
- **Doxygen** - Documentation generation
- **Git / GitHub** - Version control

### Getting Started

#### Prerequisites
- JDK 17+ (or compatible Java version configured on your path)

#### Running the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/Pattxx/QuizEz_Project.git
   ```
2. Navigate into the project directory:
   ```bash
   cd QuizEz_Project
   ```
3. Run the application using the Maven wrapper:
   - On Windows (Command Prompt):
     ```cmd
     mvnw.cmd javafx:run
     ```
   - On Linux/macOS:
     ```bash
     ./mvnw javafx:run
     ```
