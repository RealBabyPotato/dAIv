# dAIv - Your AI SMS Companion

Welcome to dAIv, your personal AI SMS companion! Designed to streamline your communication experience, dAIv offers a range of features including intelligent responses, reports, reminders, and much more, all through simple text messaging.

#### [Demonstration link on YouTube](https://youtu.be/ws8h3RK7JFg)

## Features

- **Intelligent Responses**: dAIv leverages cutting-edge AI technology to provide intelligent responses to your messages, making conversations more engaging and efficient. Whether you need quick information, advice, or a friendly chat, dAIv is here to help.
- **Reminders**: Never forget important tasks or appointments again. dAIv can help you set reminders and keep track of your schedule effortlessly. Simply text your reminder request, and dAIv will handle the rest.
- **Reports**: Generate and receive detailed reports via SMS, tailored to your needs. Whether it's a summary of your activities or specific data points, dAIv can compile and send the information directly to your phone.
- **Time Management**: Ask dAIv for the current time or set time-based alerts to stay punctual and organized. Whether you're tracking your day or coordinating with others, dAIv helps you manage your time effectively.

## Integrations

### Twilio (SMS Handling)
dAIv uses Twilio to handle SMS messages. Twilio is a powerful cloud communications platform that enables developers to integrate messaging, voice, and video communication into their applications.

- **Twilio Documentation**: Explore Twilio's documentation to learn more about how it works and how to integrate it into your projects.
    - [Twilio Documentation](https://www.twilio.com/docs/libraries/reference/twilio-java/)

### OpenAI (Intelligence)
To power its intelligent responses, dAIv relies on OpenAI, a leading AI research lab. OpenAI's models are trained on vast amounts of data and can generate human-like text responses across various tasks.

- **Library**: TheoKanning's openai-java library provides a convenient way to interact with OpenAI's API in Java.
    - [openai-java Library](https://github.com/TheoKanning/openai-java)
- **OpenAI Documentation**: Dive deeper into OpenAI's capabilities and explore how you can leverage its AI models in your applications.
    - [OpenAI Documentation](https://platform.openai.com/docs/overview)

### GSON (Data Handling)
dAIv integrates GSON to backup and store database information on users, their events, names, numbers, and more, ensuring data persistence and easy retrieval.

- **GSON Documentation**: Learn more about GSON and how to use it for JSON serialization and deserialization in Java.
    - [GSON Documentation](https://github.com/google/gson)

## Installation & Setup

### Requirements

- Maven (4+)
- Java (17+)
- Git
- Twilio API key
- OpenAI API key
- A server to receive webhooks (local or remote)

### Running with GitHub Codespaces (recommended)

If you do not already have a dedicated server, you can use GitHub Codespaces to run the server. You can directly run the `Main.java` file from the Codespace environment. Forward port 8000 in the codespace, set it's visibility to public, and set Twilio's webhook URL to the public address of your codespace running on port 8000.

### Steps to Run Locally

1. **Clone the Repository**:
    ```sh
    git clone https://github.com/DaveSMUS/dAIv.git
    cd dAIv
    ```

2. **Set Up Local Server**:
    ```sh
    chmod +x server.py
    ./server.py
    ```

3. **Link Webhook in Twilio**:
    - Go to the Twilio console and set the webhook URL to your server's address.

    > Note: Running the server locally will not allow you to receive messages unless you also listen for webhooks on your server.

### Running on Windows

1. **Open Command Prompt or Powershell**:
    ```sh
    git clone https://github.com/DaveSMUS/dAIv.git
    cd dAIv
    ```

2. **Build Classpath**:
    ```sh
    mvn dependency:build-classpath
    ```

3. **Package the Application**:
    ```sh
    mvn package
    ```

4. **Run the Application**:
    ```sh
    java -cp target/dAIV-$VERSION.jar:$CLASSPATH Main.java
    ```

## Use Cases

dAIv is designed to be versatile and useful in various scenarios. Here are some practical use cases:

- **Personal Assistant**: Use dAIv to manage your daily tasks, set reminders, and keep track of important events. With its intelligent responses, dAIv can also provide helpful information on-the-go.
- **Business Tool**: dAIv can be integrated into business workflows to automate reminders for meetings, deadlines, and follow-ups. It can also generate reports for team members, ensuring everyone stays informed.
- **Customer Support**: Enhance customer service by using dAIv to provide quick and accurate responses to customer inquiries via SMS. Its ability to handle multiple conversations simultaneously ensures efficient support.
- **Event Management**: Organize events more effectively with dAIv's reminder and reporting features. Send reminders to attendees and receive updates on event preparations and participation.

## Community

Join our Discord server to connect with other users, share experiences, and get support for dAIv.

- [Discord Server](https://discord.gg/ECNegFY9KH)

---

With dAIv, simplify your communication tasks and experience the power of AI at your fingertips. Try it out today!
