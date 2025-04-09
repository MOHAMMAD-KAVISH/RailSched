---

```markdown
# 🚆 RailSched – Railway Rake Forecasting & Scheduling App

RailSched is a smart Android app designed for **forecasting and scheduling railway rakes**. It helps railway planners and operators improve rake utilization, reduce delays, and manage resources efficiently with modern tools and a clean UI built using **Material Design 3**.

---

## 🎥 Demo Video

Watch the full app demo here:  
📽️ [RailSched Demo Video](https://drive.google.com/file/d/1PiqPMoGB9ggq5W1SmmHF-Bztc0xzF5YL/view?usp=sharing)

---

## ✨ Features

- 🔐 **Login System with Remember Me**
  - Auto-fills saved credentials using `SharedPreferences`.

- 🏠 **Modern Home Screen**
  - Built with ViewPager2, TabLayout, and Navigation Drawer.

- 📅 **Schedule Rake**
  - Select dates using a beautiful `DatePickerDialog`.

- 🔮 **Forecast Fragment**
  - Visual insights into upcoming rake availability and usage.

- ⚙️ **Settings Page**
  - Dark mode toggle
  - Language switch (localization-ready)
  - Logout button to clear saved login

- ⏰ **Alarm Notifications**
  - Schedule reminders using `AlarmManager` and `NotificationManager`.

- 🚉 **Custom Train Progress Bar**
  - Smooth animated custom view showing rake movement visually.

- 💾 **Save & Read Logs**
  - Save operational logs to internal storage on button click.

---

## 🧰 Tools & Tech Used

| Tool | Purpose |
|------|---------|
| Kotlin | Main app language |
| Material Design 3 | UI components and dynamic color |
| SharedPreferences | Save user login and app settings |
| Navigation Component | Manage fragments and drawer |
| ViewPager2 + TabLayout | Tab navigation between Forecast, Schedule, Resource |
| AlarmManager + PendingIntent | Schedule future notifications |
| NotificationManager | Display reminder alerts |
| DatePickerDialog | User-friendly date selection |
| Internal Storage | Save and read logs |
| Canvas API | Custom train progress bar drawing |
| AppCompat + Jetpack | Modern Android app architecture |

---

## 📁 Folder Structure

```
RailSched/
│
├── activities/
│   ├── LoginActivity.kt
│   └── HomeActivity.kt
│
├── fragments/
│   ├── ForecastFragment.kt
│   ├── ScheduleFragment.kt
│   ├── ResourceFragment.kt
│   └── SettingsFragment.kt
│
├── views/
│   └── TrainProgressBar.kt
│
├── utils/
│   ├── SharedPrefHelper.kt
│   ├── AlarmScheduler.kt
│   └── FileIOHelper.kt
│
├── res/
│   ├── layout/
│   ├── values/
│   ├── drawable/
│   └── mipmap/
│
└── AndroidManifest.xml
```

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/railsched.git
   ```

2. Open in **Android Studio**.

3. Run on emulator or real device (API 26+ recommended).

4. Use any email/password to log in and explore the features!

---

## 🔒 Persistent Login & Logout

- Uses `SharedPreferences` to store:
  - Last logged-in email and password
  - Dark mode preference
  - Selected language
- Logout clears stored credentials and navigates back to login.

---

## 📌 Future Scope

- Firebase backend for real-time rake tracking
- Admin dashboard for rake entry
- API integration for live rail data
- Analytics and charts for usage stats

---

## 🙌 Credits

Made with 💙 by [Mohammad Kavish]  
RailSched – Simplifying railway logistics with code.

---

```

