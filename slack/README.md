# Slack plugin

For sending messages to Slack.

## Capabilities

This Slack plugin has two action:

1. Send message to Slack channel.
2. Send message to Slack channel with attachment.

## Example application

This project also contains a working example application which is meant to showcase the slack plugin.

### Running the example application

#### Start docker

Make sure docker is running. Then use the following commands:

```shell
cd slack/docker
docker compose up
cd ../..
```

#### Start frontend

```shell
cd slack/frontend
npm install
npm run start
cd ../..
```

#### Start backend

By gradle script:

`Plugins -> slack -> backend -> task -> application -> bootRun`

Or use commend line:

```shell
cd slack/backend
./gradlew -p app bootRun
cd ../..
```

## Source code

The source code is split up into 2 modules:

1. [Frontend](/frontend)
2. [Backend](/backend)
