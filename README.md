# Climate Monitoring
A climate parameter monitoring system provided by monitoring centers that can make data on their area of interest available to environmental operators and ordinary citizens

## Download instructions
- To clone the repository use the following git command: `git clone https://github.com/adellafrattina/ClimateMonitoring.git`

### Windows
- Make sure you have installed and  **available in PATH**:
  * [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) or higher
  * [Apache Maven 3.9.8](https://maven.apache.org/download.cgi?.)	

- **Windows 10** or higher (hasn't been tested on previous versions of Windows)
- OpenGL 3.0 compatible GPU
- At least 800 x 600 screen resolution size

## How to Use
This application allows registered users (**operators**), to record and add climatic parameters to the monitoring center they are employed in.
Common users can search for a specific area and see the climatic detections submitted by operators.

**As a common user** you can search for your area of interest in the search box that pops up after the application successfully connects to the server, and look for any climatic info that operators have submitted.

**As an operator**, including common users' functions, you can register and be employed in a monitoring center, submit climatic parameters to the database, edit your personal information, and much more.

|                |Common User                          |Operator              |
|----------------|-------------------------------|-----------------------------|
|Search for an area|✅            | ✅       |
|View climatic parameters          |✅            |✅            |
|Register to the application          |✅|✅
|Login to the application          |❌|✅
|Submit climatic parameters          |❌|✅
|Edit personal information          |❌|✅
|Create a monitoring center          |❌|✅
|Add new geographic areas          |❌|✅

## Contribute to the development

1. Fork the repository
2. Clone the fork to your computer `git clone https://github.com/("your username")/ClimateMonitoring`
3. Create a new branch from the main one `git checkout -b ("your branch name")`
4. Write code and commit
5. Push commits to the remote repository `git push origin ("your branch name")`
6. Go on your fork and create a pull request
7. Wait for an admin to approve and merge your code
8. If your code was not approved, improve it and start from `4`
9. If your code was approved, you can delete your branch and repeat the process from `3`
