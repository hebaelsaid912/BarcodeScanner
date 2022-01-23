# Barcode Scanner

Barcode Scanner is an app for scan barcode or QRcode for products and check expired ones from others.
this app read code with text structure "Product Name _ Category _ Expired Date"
Expired date with text structure "yyyy-MM-dd"

[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=26)


## App Screen Recording

full vedio [here](https://drive.google.com/file/d/1XIycDWZCxdm6aK4zCXRFUq5tdokyeQpO/view?usp=sharing).


https://user-images.githubusercontent.com/72816466/150695979-bfd17542-8447-4a3e-8dc4-4fdbf0088e0a.mp4


## Code Installation

You can clone code and run it using :

``
  IDE : Android Studio
  Gradle Version : 7.0.2
  Compile Sdk : 31
``
## Tech stack & Open-source libraries
- Minimum SDK level 26
- Kotlin based + Coroutines for asynchronous.
- StateFlow - emit state updates and emit values to consumers.
- Lifecycle - dispose of observing data when lifecycle state changes.
- ViewModel - UI related data holder, lifecycle aware.
- Architecture
    - MVVM Architecture ( DataBinding - ViewModel ).
- Scalable Unit 
  - text size.
  - unit size.
- Room Database - local database to keep displaying scanned items even if the app is closed or the phone is rebooted.
- Easy Permission - access phone camera and storage.
- zxing android embedded - scanned items.
- Shimmer Effect - add shimmer effects when loading data.
- Testing
  - Local Unit Tests.
  - Instrumented Unit Tests.
  - UI tests using ( Espresso Test Recorder ).


## Release APK


Go to the [App Release](https://github.com/hebaelsaid912/BarcodeScanner/tree/main/app/release) to download the latest APK.

 
