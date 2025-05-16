# Java Schedule Manager 🗓️

A simple command-line based schedule manager written in Java.  
It supports event registration, conflict detection, searching, deletion, and real-time notifications.

## 📁 Project Structure

src/

├── Main.java

├── ScheduleManager.java

├── model/

│ ├── Event.java

│ └── Alarm.java

├── exception/

│ ├── DuplicateEventException.java

│ ├── AlarmException.java

│ └── TimeFormatException.java

├── notification/

│ ├── ConsoleNotifier.java

│ └── Notifiable.java


## ✨ Features

- 📆 일정 등록 (중복 검사 포함)
- 🔍 날짜 기반 검색
- ❌ 일정 삭제
- ⏰ 실시간 알림 (이벤트 발생 시간에 콘솔 알림)
- ❗ 예외 처리 클래스 분리

## 🛠️ Requirements

- Java 11+
- IntelliJ / Eclipse or any terminal + javac setup

## ▶️  실행 방법

javac src/*.java src/*/*.java
java -cp src Main  __ 우클릭 -> Run as -> java Application
