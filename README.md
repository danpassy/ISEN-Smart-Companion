# ISEN Smart Companion 📱🎓

**Your Personal Student Assistant for ISEN Toulon**

## 📝 Project Overview

The **ISEN Smart Companion** is a personal assistant application designed to help ISEN Toulon students efficiently manage their academic and social life. The app integrates AI-powered features to provide personalized advice for organizing courses, managing schedules, participating in events, and optimizing free time. Developed using **Jetpack Compose** for a modern and intuitive UI, this app aims to balance studies and extracurricular activities (BDE, BDS, etc.).



## 🚀 Features

- 🎨 **User-friendly Interface** – Built with Jetpack Compose for a modern Android experience.
- 🤖 **AI Integration** – Get personalized suggestions using the Google Gemini AI API.
- 📅 **Event Management** – Discover, view, and manage ISEN events like BDE evenings, Galas, and more.
- 🔔 **Event Notifications** – Stay informed with reminders for your pinned events.
- 📚 **Interaction History** – Save and review previous AI interactions locally using Room Database.
- 🔗 **Web Service Integration** – Real-time event updates via a Firebase web service.



## 📂 Project Structure

```
├── MainActivity.kt         # Home screen with AI assistant
├── EventsScreen.kt         # Displays a list of events
├── EventDetailActivity.kt  # Shows details of a selected event
├── HistoryScreen.kt        # Displays the history of AI interactions
├── models/
│   └── Event.kt            # Data model for events
├── network/
│   ├── ApiService.kt       # Retrofit service interface
│   └── RetrofitClient.kt   # Initializes Retrofit instance
├── database/
│   ├── InteractionDao.kt   # DAO for Room database
│   ├── Interaction.kt      # Entity class for Room
│   └── AppDatabase.kt      # Database instance
└── utils/
    └── NotificationHelper.kt  # Handles notifications
```



## 🛠️ Getting Started

### Prerequisites
- **Android Studio** (Latest version recommended)
- **Gradle** 7.0+
- Minimum API Level: **25 (includes 96% of users)**

### Installation Steps
1. Clone the repository:
   ```
   git clone https://github.com/danpassy/ISEN-Smart-Companion.git

   ```
2. Open the project in **Android Studio**.
3. Configure your AI API key in `local.properties`:
   ```
   AI_API_KEY=your_gemini_api_key
   ```
4. Run the app on an emulator or your Android phone.



## 🔍 Key Functionalities

### Part I: UI Development
- Set up the basic UI with input fields and a mock AI response.
- Manage state with `remember` and `MutableState`.

### Part II: Navigation & Events
- Implement bottom navigation for Home, Events, and History screens.
- Use `LazyColumn` to display event lists.
- Navigate to detailed event pages with passed parameters.

### Part III: Web Services & AI Integration
- Fetch real-time event data using **Retrofit**.
- Integrate **Google Gemini AI** for dynamic responses.

### Part IV: Local Storage & Notifications
- Use **Room Database** to save and display chat history.
- Implement notifications for pinned events.


## 📚 Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose/documentation)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Google AI Client SDK](https://developer.android.com/ai/google-ai-client-sdk)



## 🤝 Contributing

1. Fork the project
2. Create a new branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request


## 📧 Contact

Developed by **Dan Boussougou**  
For any inquiries, please contact danbouss22@gmail.com.
