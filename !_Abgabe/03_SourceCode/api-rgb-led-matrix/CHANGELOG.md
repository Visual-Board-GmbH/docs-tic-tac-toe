All notable changes to this project will be documented in this file.  
This project adheres to [Semantic Versioning](http://semver.org/).

## 1.0.0 - 2020-12-29
### Changed
- Release 1.0.0

### Added
- some JUnit-Tests

## 0.5.1 - 2019-12-29
### Changed
- refactored inside `/config/serialport`

## 0.5.0 - 2019-12-28
### Added
- save last displayed image on the excecution folder
- load last displayed image when running the API
- image transformations:
    - fit image (black bars)

## 0.4.0 - 2019-12-26
### Added
- image transformations: 
    - sizing (upscale and downscale)

## 0.3.0 - 2019-12-23
### Added
- endpoint `POST [IP:PORT]/v1/image-upload` (upload and show image on RGB Matrix)
- endpoint `POST [IP:PORT]/v1/image-url` (download by url / path and show image on RGB Matrix)
- image transformations: 
    - image to byte code 
    - snakeline
    - rotate
- some JUnit-Tests

### Changed
- serial port gateway opens serial port and keeps it open instead of closing it each time
- "hello world" code removed
- clean code

## 0.2.0 - 2019-11-11
### Added
- Swagger Docs `[IP:PORT]/v2/api-docs`
- Swagger UI `[IP:PORT]/docs`

## 0.1.0 - 2019-11-11
### Added
- initial changelog
- initial Java Maven Spring Boot Setup
- initial `Hello World` REST endpoint (Displays an image on the RGB panel)
