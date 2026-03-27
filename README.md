# 📞 TrueCalling - Android Calling App

TrueCalling is an Android application built using **Kotlin + Jetpack Compose** that provides real phone calling functionality using Android system APIs.  
The app allows users to **dial numbers, access contacts, view call logs, and check the last call duration**.

---

## 🚀 Features

### ☎️ Dialer
- Custom dial pad UI  
- Make real phone calls using Android Intent (`ACTION_CALL`)  
- Runtime permission handling  

### 👥 Contacts
- Fetches device contacts using `ContactsContract`  
- Displays name and phone number  
- Tap on a contact to directly call  

### 📜 Call Logs
- Fetches real call logs using `CallLog.Calls`  
- Displays:
  - Contact name (if available)  
  - Phone number  
  - Call type (Incoming / Outgoing / Missed)  
  - Date & time  
  - Call duration  
- Tap on any log to call again  

### ⏱️ Last Call Duration
- Displays the **duration of the most recent call**  
- Updates automatically when a call ends  
- Uses `TelephonyManager` to detect call state  

---

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture:

```
UI (Compose Screens)
↓
ViewModel (State + Logic)
↓
Repository (Data Source)
↓
Android System APIs
```

### Layers:
- **UI Layer** → Jetpack Compose Screens  
- **ViewModel** → State management using `StateFlow`  
- **Repository** → Handles data fetching from system APIs  
- **Model** → Data classes  

---

## 🛠️ Tech Stack

- Kotlin  
- Jetpack Compose  
- MVVM Architecture  
- StateFlow  
- Android System APIs  
  - ContactsContract  
  - CallLog  
  - TelephonyManager  

---

## 🔐 Permissions Used

- `CALL_PHONE` → To make phone calls  
- `READ_CONTACTS` → To access contacts  
- `READ_CALL_LOG` → To fetch call logs  
- `READ_PHONE_STATE` → To detect call state changes  

---

## 📱 How It Works

1. User interacts with UI (Dialer / Contacts / Logs)  
2. ViewModel processes actions  
3. Repository fetches data from Android system APIs  
4. UI updates automatically via StateFlow  

---

## ⚠️ Note

- This app performs **real phone calls**, so it must be tested on a **physical device**  
- Some features may not work on emulators  

---


## 👨‍💻 Developer

**Ronit Barnawal**
