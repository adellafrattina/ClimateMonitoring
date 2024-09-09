<p align="center">
  <img width="500" src="https://files.catbox.moe/9gez28.png" alt="i lov u lorees <3">
</p>


A climate parameter monitoring system provided by monitoring centers that can make data on their area of interest available to environmental operators and ordinary citizens

## Download instructions
- To clone the repository use the following git command: `git clone https://github.com/adellafrattina/ClimateMonitoring.git`
- To download a clean copy of the database with the areas already loaded in ([Click!](https://files.catbox.moe/5sv68u.zip))

## Build instructions
### Windows
- Make sure you have installed and  **available in PATH** [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or higher
- **Build client**:
	* execute `scripts\maven\mvnw.cmd -am --projects climate-monitoring-client clean install`
	_or_
	* run `scripts\build\windows\build-client.bat`
- **Build server**:
	* execute `scripts\maven\mvnw.cmd -am --projects climate-monitoring-server clean install`
	_or_
	* run `scripts\build\windows\build-server.bat`
### Linux
- Install dependencies: [openjdk17](https://openjdk.org/install/) or higher (package name could vary from system to system)
- **Build client**:
	* execute `scripts\maven\mvnw -am --projects climate-monitoring-client clean install`
	_or_
	* run `scripts\build\linux\build-client.bat`
- **Build server**:
	* execute `scripts\maven\mvnw -am --projects climate-monitoring-server clean install`
	_or_
	* run `scripts\build\linux\build-server.bat`
### macOS
- Check dependencies from **Linux** section above and make sure you have them installed
- **Build client**:
	* execute `scripts\maven\mvnw -am --projects climate-monitoring-client clean install`
	_or_
	* run `scripts\build\macos\build-client.bat`
- **Build server**:
	* execute `scripts\maven\mvnw -am --projects climate-monitoring-server clean install`
	_or_
	* run `scripts\build\macos\build-server.bat`

## Functionalities
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

## System requirements
|                |Minimum                          |Recommended              |
|----------------|-------------------------------|-----------------------------|
|OS (Windows)|32-bit Windows 10            | 64-bit Windows 10 or higher       |
|OS (Linux)          |64-bit Linux distribution            |64-bit Ubuntu 23.04 or higher, Arch Linux 2024.08.01 (X11)            |
|OS (macOS)          |64-bit macOS Monterey 12.01|64-bit macOS Monterey 12.01 or higher
|OS (macOS arm64)          |64-bit macOS Sonoma 14.6 (M1 CPU)|64-bit macOS Sonoma 14.6 (M1 CPU) or higher
|Processor          |Intel i5-7200U, AMD A8-6500|Intel i7-8750H, AMD Ryzen 5 3600
|Memory          |500 MB RAM|1.5 GB RAM
|Graphics          |Any integrated GPU that supports OpenGL|Discrete GPU that supports OpenGL
|OpenGL          |Version 3.0|Version 3.0 or higher
|Storage          |15 MB available space|30 MB available space or higher
|Screen resolution          |800 x 600|1920 x 1080 or higher

**Note:** _the specs listed above come from systems that have been used to run the application, if you don't see your system in the specs it means that it may or may not work_

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
