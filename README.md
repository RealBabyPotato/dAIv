# dAIv - Your AI SMS Companion

dAIv is your personal AI SMS companion designed to streamline your communication experience. It offers a range of features including intelligent responses, Google searches, reminders, and much more, all through simple text messaging.

## Features

- **Intelligent Responses**: dAIv leverages cutting-edge AI technology to provide intelligent responses to your messages, making conversations more engaging and efficient.

- **Google Search Integration**: Need information? Just ask dAIv to perform a Google search for you and get instant answers right within your SMS conversation.

- **Reminders**: Never forget important tasks or appointments again. dAIv can help you set reminders and keep track of your schedule effortlessly.

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

## Install & Run
### Requirements:
Maven (4+)
Java (17+)
git
### Additional requirements for server.py helper script
GNU coreutils
A Unix-based server
curl
python3 (for install script)

1. Run `git clone https://github.com/DaveSMUS/dAIv.git` or clone the repository from Github Desktop.

2. Run `cd dAIv && chmod +x server.py`

3. Run `./server.py` to run your dAIv server! dAIv will be built and run automatically!

### But I want to run it on Windows!
Never fear, it is possible, but a bit more involved.

1. Open Command Prompt or Powershell, I don't know which is better

2. Run `git clone https://github.com/DaveSMUS/dAIv.git` or clone the repository from Github Desktop, and enter the directory in Command Prompt.

3. Run `mvn dependency:build-classpath` and copy the really long line that should look something like this:
`/home/will/.m2/repository/com/theokanning/openai-gpt3-java/service/0.18.2/service-0.18.2.jar:/home/will/.m2/repository/com/theokanning/openai-gpt3-java/client/0.18.2/client-0.18.2.jar...`

4. Run `mvn package`.

5. Find the latest version of dAIv in the `target/` directory and take note of it.

5. Run `java -cp target/dAIV-$VERSION.jar:$CLASSPATH Main.java`

Please note, I am completely guessing on this part - don't @ me.

## Community

Join our Discord server to connect with other users, share experiences, and get support for dAIv.
- [Discord Server](https://discord.gg/ECNegFY9KH)

---

With dAIv, simplify your communication tasks and experience the power of AI at your fingertips. Try it out today!
